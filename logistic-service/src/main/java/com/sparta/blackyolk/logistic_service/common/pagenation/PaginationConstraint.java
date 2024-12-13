package com.sparta.blackyolk.logistic_service.common.pagenation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PaginationConstraint {
    int[] availableSize() default {10, 30, 50};
    int defaultSize() default 10;
    String defaultSort() default "createdAt";
    String defaultDirection() default "DESC";
}
