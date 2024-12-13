package com.sparta.blackyolk.logistic_service.hub.framework.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CenterValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCenter {

    String message() default """
        유효하지 않은 센터 지역입니다. 다음 지역 중 하나를 선택해주세요:
          1. "서울특별시 센터",
          2. "경기 북부 센터",
          3. "경기 남부 센터",
          4. "부산광역시 센터",
          5. "대구광역시 센터",
          6. "인천광역시 센터",
          7. "광주광역시 센터",
          8. "대전광역시 센터",
          9. "울산광역시 센터",
          10. "세종특별자치시 센터",
          11. "강원특별자치도 센터",
          12. "충청북도 센터",
          13. "충청남도 센터",
          14. "전북특별자치도 센터",
          15. "전라남도 센터",
          16. "경상북도 센터",
          17. "경상남도 센터"
        """;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
