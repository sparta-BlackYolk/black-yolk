package com.sparta.blackyolk.logistic_service.hubroute.application.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TimeSlotWeightMapper {

    private static final Map<String, Double> TIME_SLOT_WEIGHTS;

    static {
        TIME_SLOT_WEIGHTS = Map.of(
            "22:00-05:00", 1.0,
            "05:00-07:00", 1.2,
            "07:00-09:00", 2.0,
            "09:00-17:00", 1.5,
            "17:00-20:00", 2.0,
            "20:00-22:00", 1.2
        );
    }

    public double getWeight(String timeSlot) {
        return TIME_SLOT_WEIGHTS.getOrDefault(timeSlot, 1.0);
    }

    public Map<String, Double> getTimeSlots() {
        return TIME_SLOT_WEIGHTS;
    }

    public String getCurrentTimeSlot(LocalDateTime now) {
        String time = now.format(DateTimeFormatter.ofPattern("HH:mm"));
        String timeSlot = getTimeSlots().keySet().stream()
            .filter(slot -> isTimeInSlot(slot, time))
            .findFirst()
            .orElse("22:00-05:00");

        log.info("[최단 경로 탐색] 시간대 : {}, 현재 시각: {}", timeSlot, time);
        return timeSlot;
    }

    private boolean isTimeInSlot(String slot, String time) {
        String[] timeRange = slot.split("-");
        String startTime = timeRange[0];
        String endTime = timeRange[1];
        return isCrossingMidnight(startTime, endTime)
            ? time.compareTo(startTime) >= 0 || time.compareTo(endTime) < 0
            : time.compareTo(startTime) >= 0 && time.compareTo(endTime) < 0;
    }

    private boolean isCrossingMidnight(String startTime, String endTime) {
        return endTime.compareTo(startTime) < 0;
    }
}
