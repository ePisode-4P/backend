package com.pppp.recording.dto;

import com.pppp.recording.model.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;

@Data
@Getter
@Setter
public class AuthDTO {
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "이메일 주소를 입력해주세요.")
    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    private String email;

    public AuthDTO toAuthDTO(UserEntity userEntity) {
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail(userEntity.getEmail());
        authDTO.setPassword(userEntity.getPassword());
        return authDTO;
    }
}
