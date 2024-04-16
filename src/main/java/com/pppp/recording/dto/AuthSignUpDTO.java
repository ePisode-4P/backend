package com.pppp.recording.dto;

import com.pppp.recording.model.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString
@Data
@Getter
public class AuthSignUpDTO {

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 20, message = "닉네임은 2자이상 20자 이하로 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "이메일 주소를 입력해주세요.")
    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    private String email;
    private String favorite;
    private String address;
    private String mbti;

    public UserEntity toUserEntity() {
        return UserEntity.builder().email(email).username(username).password(password).mbti(mbti).build();
    }
}