package com.sparta.blackyolk.logistic_service.common.pagenation;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class PaginationArgumentResolver extends PageableHandlerMethodArgumentResolver {

    @Override
    public Pageable resolveArgument(
        MethodParameter methodParameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) {
        Pageable pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);

        if (methodParameter.hasMethodAnnotation(PaginationConstraint.class)) {
            return validatePageableOrDefault(methodParameter, pageable);
        }

        return pageable;
    }

    private Pageable validatePageableOrDefault(MethodParameter methodParameter, Pageable pageable) {
        PaginationConstraint constraint = methodParameter.getMethodAnnotation(PaginationConstraint.class);

        Sort sort = resolveSortOrDefault(pageable, constraint);
        int size = resolveSizeOrDefault(pageable, constraint);

        return PageRequest.of(pageable.getPageNumber(), size, sort);
    }

    private Sort resolveSortOrDefault(Pageable pageable, PaginationConstraint constraint) {
        if (pageable.getSort().isSorted()) {
            return pageable.getSort();
        }
        return Sort.by(Sort.Direction.fromString(constraint.defaultDirection()), constraint.defaultSort());
    }

    private int resolveSizeOrDefault(Pageable pageable, PaginationConstraint constraint) {
        int requestedSize = pageable.getPageSize();
        for (int availableSize : constraint.availableSize()) {
            if (requestedSize == availableSize) {
                return requestedSize;
            }
        }
        return constraint.defaultSize();
    }
}
