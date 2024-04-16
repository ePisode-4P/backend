package com.pppp.recording.controller;

import com.pppp.recording.auth.TokenProvider;
import com.pppp.recording.dto.AuthDTO;
import com.pppp.recording.dto.AuthSignUpDTO;
import com.pppp.recording.model.CategoryEntity;
import com.pppp.recording.model.FavoriteEntity;
import com.pppp.recording.model.UserEntity;
import com.pppp.recording.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final TokenProvider tokenProvider;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody AuthDTO authDTO) {
        UserEntity user = authService.login(authDTO);
        if (user != null) {
            final String accessToken = tokenProvider.createAccessToken(user);
            final String refreshToken = tokenProvider.createRefreshToken(user);

            return ResponseEntity.ok().header("access-token", accessToken).header("refresh-token", refreshToken).body(authDTO);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/test")
    public void test() {
        System.out.println("401 test");
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AuthSignUpDTO authSignUpDTO) {
        if (!authService.checkEmailDuplicate(authSignUpDTO.getEmail())) {
            authService.save(authSignUpDTO.toUserEntity());
            Optional<UserEntity> savedUser = authService.findByUserId(authSignUpDTO.getEmail());
            for(String favorite:authSignUpDTO.getFavorite()) {
                CategoryEntity category = authService.findByName(favorite);

                savedUser.ifPresent(user -> authService.saveFavorite(user, category));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

//    @GetMapping("/token")
//    public ResponseEntity<?> validToken() {
//        authService
//    }

}
