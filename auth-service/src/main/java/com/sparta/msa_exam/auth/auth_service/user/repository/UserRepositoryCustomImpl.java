package com.sparta.msa_exam.auth.auth_service.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.msa_exam.auth.auth_service.user.entity.QUser;
import com.sparta.msa_exam.auth.auth_service.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<User> findUserByDynamicCondition(Long userId, String username, String email, String slackId) {
        QUser user = QUser.user;

        return Optional.ofNullable(
                queryFactory.selectFrom(user)
                        .where(
                                userId != null ? user.id.eq(userId) : null,
                                username != null ? user.username.eq(username) : null,
                                email != null ? user.email.eq(email) : null,
                                slackId != null ? user.slackId.eq(slackId) : null
                        )
                        .fetchOne()
        );
    }
}
