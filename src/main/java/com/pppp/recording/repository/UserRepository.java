package com.pppp.recording.repository;

import com.pppp.recording.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
    boolean existsByEmail(String email);
    // 이메일로 회원 정보 조회
    Optional<UserEntity> findByEmail(String email);
}
