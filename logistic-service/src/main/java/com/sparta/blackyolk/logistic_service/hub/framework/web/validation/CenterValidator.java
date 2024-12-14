package com.sparta.blackyolk.logistic_service.hub.framework.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;

public class CenterValidator implements ConstraintValidator<ValidCenter, String> {

    // 허용된 센터 지역 목록
    private static final Set<String> ALLOWED_CENTERS = Set.of(
          "서울특별시 센터",
          "경기 북부 센터",
          "경기 남부 센터",
          "부산광역시 센터",
          "대구광역시 센터",
          "인천광역시 센터",
          "광주광역시 센터",
          "대전광역시 센터",
          "울산광역시 센터",
          "세종특별자치시 센터",
          "강원특별자치도 센터",
          "충청북도 센터",
          "충청남도 센터",
          "전북특별자치도 센터",
          "전라남도 센터",
          "경상북도 센터",
          "경상남도 센터"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null을 허용
        }
        return ALLOWED_CENTERS.contains(value);
    }
}
