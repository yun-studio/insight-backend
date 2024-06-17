package com.yunstudio.insight.domain.user.repository;

import com.yunstudio.insight.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByProviderId(String providerId);
}