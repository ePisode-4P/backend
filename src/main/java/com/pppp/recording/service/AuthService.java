package com.pppp.recording.service;

import com.pppp.recording.dto.AuthDTO;
import com.pppp.recording.model.UserEntity;
import com.pppp.recording.repository.UserRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Getter
@Setter
@RequiredArgsConstructor
@Service
@ToString
public class AuthService {
    private final UserRepository userRepository;

    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
    public void save(UserEntity userEntity) {
        System.out.println(userEntity);
        userRepository.save(userEntity);
    }

    public UserEntity login(AuthDTO authDTO) {
        /*
            1. 회원이 입력한 이메일로 DB에서 조회
            2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
        */
        Optional<UserEntity> byUserEmail = userRepository.findByEmail(authDTO.getEmail());
        if (byUserEmail.isPresent()) {
            // 조회 결과가 있다 (해당 이메일을 가진 회원 정보가 있다.)
            UserEntity userEntity = byUserEmail.get();
            if (userEntity.getPassword().equals(authDTO.getPassword())) {
                // 비밀번호 일치
                // entity -> dto 변환 후 리턴
                return userEntity;
            } else {
                // 비밀번호 불일치
                return null;
            }

        } else {
            // 조회 결과가 없다. (해당 이메일을 가진 회원이 없다.)
            return null;
        }
    }
}
