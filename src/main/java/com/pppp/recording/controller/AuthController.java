package com.pppp.recording.controller;

import com.pppp.recording.auth.TokenProvider;
import com.pppp.recording.dto.AuthDTO;
import com.pppp.recording.dto.AuthSignUpDTO;
import com.pppp.recording.model.CategoryEntity;
import com.pppp.recording.model.FavoriteEntity;
import com.pppp.recording.model.UserEntity;
import com.pppp.recording.security.TokenBlacklist;
import com.pppp.recording.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
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

    @GetMapping("/token")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token) {
        String authToken = token.substring(7);
        System.out.println(authToken);
        if (!TokenBlacklist.isBlacklisted(authToken) && tokenProvider.validateToken(authToken)) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid");
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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        try {
            String authToken = token.substring(7);
            TokenBlacklist.addToBlacklist(authToken);
            tokenProvider.expireToken(authToken);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

//    @GetMapping("/token")
//    public ResponseEntity<?> validToken() {
//        authService
//    }

}
