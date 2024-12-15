package com.sparta.blackyolk.logistic_service.hub.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sparta.blackyolk.logistic_service.hub.data.vo.HubStatus;
import com.sparta.blackyolk.logistic_service.hub.application.domain.model.HubAddress;
import com.sparta.blackyolk.logistic_service.hub.application.domain.model.HubCoordinate;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // Jackson에게 Hub 클래스에 정의되지 않은 필드를 무시하도록 설정
public class Hub implements Serializable {
    private String hubId;
    private String hubName;
    private String hubCenter;
    private HubStatus hubStatus;
    private HubCoordinate hubCoordinate;
    private HubAddress hubAddress;
    private Long hubManagerId;
    private boolean isDeleted;
}
