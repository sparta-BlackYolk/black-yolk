package com.sparta.blackyolk.logistic_service.hubroute.application.service;

import com.sparta.blackyolk.logistic_service.common.exception.CustomException;
import com.sparta.blackyolk.logistic_service.common.exception.ErrorCode;
import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.domain.model.Node;
import com.sparta.blackyolk.logistic_service.hub.application.service.HubCacheService;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.application.port.HubRoutePersistencePort;
import com.sparta.blackyolk.logistic_service.hubroute.application.usecase.HubRoutePathUseCase;
import com.sparta.blackyolk.logistic_service.hubroute.application.util.TimeSlotWeightMapper;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRoutePathResponse;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import lombok.RequiredArgsConstructor;
import java.util.Comparator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubRoutePathService implements HubRoutePathUseCase {

    // 평균 속도 가정
    private static final double AVERAGE_SPEED_KMH = 60.0; // km/h

    private final HubRoutePersistencePort hubRoutePersistencePort;
    private final HubCacheService hubCacheService;
    private final TimeSlotWeightMapper timeSlotWeightMapper;

    private final Map<String, List<HubRoute>> hubRouteCache = new HashMap<>();
    private final Map<String, Double> distanceCache = new HashMap<>();

    @Transactional
    @PostConstruct
    public void preloadHubRoutes() {
        List<HubRoute> allRoutes = hubRoutePersistencePort.findAllHubRoutes();
        for (HubRoute route : allRoutes) {
            hubRouteCache
                .computeIfAbsent(route.getDepartureHub().getHubId(), k -> new ArrayList<>())
                .add(route);
        }
        log.info("허브 경로 캐싱 완료. 총 경로 수: {}", allRoutes.size());
    }

    @Override
    @Cacheable(cacheNames = "hub_route_path_cache",
        key = "'path:' + #departure + ' to ' + #arrival + ' in ' + #currentTimeSlot"
    )
    public HubRoutePathResponse getShortestPath(String departure, String arrival, String currentTimeSlot) {
        double timeSlotWeight = timeSlotWeightMapper.getWeight(currentTimeSlot);

        log.info("[최단 경로 탐색] 현재 시간대 : {}, 가중치: {}", currentTimeSlot, timeSlotWeight);

        Hub departureHub = hubCacheService.validateHub(departure);
        Hub arrivalHub = hubCacheService.validateHub(arrival);
        log.info("[최단 경로 탐색] 출발 허브: {}, 도착 허브: {}", departureHub.getHubName(), arrivalHub.getHubName());

        log.info("[최단 경로 탐색] {} 에서 {} 로의 최단 경로, 시간대: {}", departureHub.getHubName(), arrivalHub.getHubName(), currentTimeSlot);

        List<HubRoute> shortestPath = findShortestPath(currentTimeSlot, timeSlotWeight, departureHub, arrivalHub);

        log.info("[최단 경로 탐색] 최단 경로 : {}", shortestPath);

        return HubRoutePathResponse.toDTO(
            departureHub.getHubName(),
            arrivalHub.getHubName(),
            shortestPath,
            timeSlotWeight
        );
    }

    private List<HubRoute> findShortestPath(String currentTimeSlot, double timeSlotWeight, Hub departureHub, Hub arrivalHub) {
        PriorityQueue<Node> candidateNodes = initializeCandidateNodes(departureHub, arrivalHub);
        Map<Hub, Double> costFromStart = initializeCostFromStart(departureHub);
        Map<Hub, Node> previousNodes = new HashMap<>();

        log.info("[최단 경로 탐색] candidateNodes 크기: {}", candidateNodes.size());

        while (!candidateNodes.isEmpty()) {
            Node currentNode = candidateNodes.poll();

            if (currentNode == null) {
                log.error("[최단 경로 탐색] currentNode가 null입니다.");
                throw new CustomException(ErrorCode.PATH_NOT_EXIST);
            }

            log.info("[최단 경로 탐색] 현재 허브: {}, 현재 비용: {}", currentNode.getHub().getHubName(), currentNode.getG());

            if (currentNode.getHub().getHubId().equals(arrivalHub.getHubId())) {
                log.info("[최단 경로 탐색] 최종 허브에 도달: {}", arrivalHub.getHubName());
                return reconstructPath(previousNodes, currentNode);
            }

            log.info("[최단 경로 탐색] processNeighbors 호출 준비: currentNode={}, arrivalHub={}, candidateNodes size={}",
                currentNode.getHub().getHubName(), arrivalHub.getHubName(), candidateNodes.size());

            processNeighbors(
                currentNode,
                arrivalHub,
                candidateNodes,
                costFromStart,
                previousNodes,
                currentTimeSlot,
                timeSlotWeight
            );
        }

        log.info("[최단 경로 탐색] 허브 경로 없음 {} -> {}", departureHub.getHubName(), arrivalHub.getHubName());
        throw new CustomException(ErrorCode.PATH_NOT_EXIST);
    }

    private PriorityQueue<Node> initializeCandidateNodes(Hub departureHub, Hub arrivalHub) {
        PriorityQueue<Node> candidateNodes = new PriorityQueue<>(Comparator.comparingDouble(Node::getF));
        candidateNodes.add(new Node(departureHub, 0, heuristic(departureHub, arrivalHub)));
        log.info("[최단 경로 탐색] candidate nodes 초기화: {}", departureHub.getHubName());
        return candidateNodes;
    }

    private Map<Hub, Double> initializeCostFromStart(Hub departureHub) {
        Map<Hub, Double> costFromStart = new HashMap<>();
        costFromStart.put(departureHub, 0.0);
        log.info("[최단 경로 탐색] cost from start 초기화: {}, cost: 0", departureHub.getHubName());
        return costFromStart;
    }

    private void processNeighbors(
        Node currentNode,
        Hub arrivalHub,
        PriorityQueue<Node> candidateNodes,
        Map<Hub, Double> costFromStart,
        Map<Hub, Node> previousNodes,
        String currentTimeSlot,
        double timeSlotWeight
    ) {
        log.info("[최단 경로 탐색] 현재 경로: {}, Hub ID: {}", currentNode.getHub().getHubName(), currentNode.getHub().getHubId());

        List<HubRoute> neighbors = hubRouteCache.getOrDefault(currentNode.getHub().getHubId(), Collections.emptyList());

        log.info("[최단 경로 탐색] 허브 {}의 이웃 허브 개수: {}", currentNode.getHub().getHubName(), neighbors.size());
        if (neighbors.isEmpty()) {
            log.warn("[최단 경로 탐색] 허브 {}의 이웃 노드가 없습니다.", currentNode.getHub().getHubName());
            return;
        }

        for (HubRoute route : neighbors) {
            log.info("[최단 경로 탐색] 시간대 {}의 가중치: {}", currentTimeSlot, timeSlotWeight);

            double newCost = calculateExpectedCost(currentNode, route, timeSlotWeight, costFromStart);

            if (newCost < costFromStart.getOrDefault(route.getArrivalHub(), Double.MAX_VALUE)) {
                log.info("[최단 경로 탐색] 비용 업데이트: {}, 예상 비용: {}", route.getArrivalHub().getHubName(), newCost);

                costFromStart.put(route.getArrivalHub(), newCost);
                previousNodes.put(route.getArrivalHub(), currentNode);

                double fScore = newCost + heuristic(route.getArrivalHub(), arrivalHub);

                candidateNodes.add(new Node(route.getArrivalHub(), newCost, fScore));
            }
        }
    }

    private double calculateExpectedCost(Node currentNode, HubRoute route, double timeSlotWeight, Map<Hub, Double> costFromStart) {
        // 현재 노드까지의 비용
        double currentCost = costFromStart.getOrDefault(currentNode.getHub(), Double.MAX_VALUE);

        // 소요 시간 기반 비용 계산
        double travelTimeInMinutes = route.getDuration(); // 소요 시간(분 단위)
        double cost = currentCost + travelTimeInMinutes * timeSlotWeight;

        log.info("[최단 경로 탐색] 예상 경로 비용 {} -> {}: {} (소요 시간: {}분, 가중치: {})",
                 currentNode.getHub().getHubName(), route.getArrivalHub().getHubName(), cost, travelTimeInMinutes, timeSlotWeight);

        return cost;
    }

    private List<HubRoute> reconstructPath(Map<Hub, Node> previousNodes, Node currentNode) {
        List<HubRoute> path = new ArrayList<>();
        Node node = currentNode;

        while (node != null) {
            Node previousNode = previousNodes.get(node.getHub());
            if (previousNode != null) {
                HubRoute route = hubRoutePersistencePort
                    .findHubRouteByDepartureHubIdAndArrivalHubId(
                        previousNode.getHub().getHubId(),
                        node.getHub().getHubId()
                    ).orElseThrow(() -> new CustomException(ErrorCode.HUB_ROUTE_NOT_EXIST));

                log.info("[최단 경로 탐색] route 추가 {} -> {}",
                          previousNode.getHub().getHubName(), node.getHub().getHubName());
                path.add(route);
            }
            node = previousNode;
        }

        Collections.reverse(path);
        log.info("[최단 경로 탐색] 경로 재구성: {}", path);
        return path;
    }

    private double heuristic(Hub current, Hub goal) {
        String cacheKey = current.getHubId() + "->" + goal.getHubId();

        return distanceCache.computeIfAbsent(cacheKey, k -> {

            // 현재 허브에서 목표 허브까지의 예상 직선 거리 계산
            double distance = calculateHaversineDistance(current, goal); // 단위(m)
            double distanceInKilometers = distance / 1000.0; // km로 변환

            double estimatedTravelTime = (distanceInKilometers / AVERAGE_SPEED_KMH) * 60; // 예상 소요 시간(분 단위)

            log.info("[최단 경로 탐색] heuristic 계산 {} -> {}: 거리={}, 예상 소요 시간={}분",
                 current.getHubName(), goal.getHubName(), distance, estimatedTravelTime);

            return estimatedTravelTime;
        });
    }

    private double calculateHaversineDistance(Hub current, Hub goal) {
        final double R = 6371000; // 지구 반지름 (단위: m)

        // 위도와 경도를 라디안으로 변환
        double lat1 = Math.toRadians(current.getHubCoordinate().getAxisX().doubleValue());
        double lon1 = Math.toRadians(current.getHubCoordinate().getAxisY().doubleValue());
        double lat2 = Math.toRadians(goal.getHubCoordinate().getAxisX().doubleValue());
        double lon2 = Math.toRadians(goal.getHubCoordinate().getAxisY().doubleValue());

        // 위도와 경도의 차이
        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;

        // Haversine 공식
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 거리 계산
        return R * c; // 결과 거리 (단위: m)
    }

    public void validateMaster(String role) {
        if (!role.equals("MASTER")) {
            throw new CustomException(ErrorCode.PATH_ACCESS_DENIED);
        }
    }
}
