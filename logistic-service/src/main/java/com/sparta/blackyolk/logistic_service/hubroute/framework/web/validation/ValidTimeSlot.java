package com.sparta.blackyolk.logistic_service.hubroute.framework.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = TimeSlotValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTimeSlot {

    String message() default """
        유효하지 않은 시간대입니다. 다음 형식과 값을 사용해야 합니다:
        - 형식: HH:mm-HH:mm
        - 허용된 값:
          1. 22:00-05:00
          2. 05:00-07:00
          3. 07:00-09:00
          4. 09:00-17:00
          5. 17:00-20:00
          6. 20:00-22:00
        """;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
