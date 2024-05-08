package com.pppp.recording.controller;

import com.pppp.recording.auth.TokenProvider;
import com.pppp.recording.dto.DiaryDTO;
import com.pppp.recording.dto.DiaryGetAllDTO;
import com.pppp.recording.dto.DiaryUpdateDTO;
import com.pppp.recording.model.DiaryEntity;
import com.pppp.recording.model.PlaceEntity;
import com.pppp.recording.model.UserEntity;
import com.pppp.recording.service.DiaryService;
import com.pppp.recording.service.KakaoMapService;
import com.pppp.recording.service.PlaceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.MethodNotAllowedException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequestMapping("diaries")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;
    private final KakaoMapService kakaoMapService;
    private final PlaceService placeService;
    private final TokenProvider tokenProvider;

    @PostMapping
    public ResponseEntity<?> saveDiary(@RequestBody DiaryDTO diaryDTO, @RequestHeader("Authorization") String token) {
        try {
            UserEntity userEntity = tokenProvider.vaildateAndGetUser(token.substring(7));
            Optional<PlaceEntity> placeEntity = placeService.findPlaceEntity(diaryDTO.getPlaceId());
            LocalDateTime localDateTime = LocalDateTime.now();

            if (placeEntity.isPresent()) {
                PlaceEntity updatePlace = placeEntity.orElse(null);
                diaryService.save(diaryDTO.toDiaryEntity(userEntity, updatePlace, localDateTime));
            } else {
                PlaceEntity kakaoPlace = kakaoMapService.findPlace(diaryDTO.getPlaceName(), diaryDTO.getX(), diaryDTO.getY());
                String id = placeService.savePlace(kakaoPlace);
                Optional<PlaceEntity> updatePlaceEntity = placeService.findPlaceEntity(id);
                updatePlaceEntity.ifPresent(place -> diaryService.save(diaryDTO.toDiaryEntity(userEntity, place, localDateTime)));
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (
    MethodNotAllowedException e) {
        // 허용되지 않은 메서드인 경우
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    } catch (Exception e) {
            System.out.println(e);
        // 기타 서버 내부 오류인 경우
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    }

    @GetMapping
    public ResponseEntity<?> getDiaries(@RequestHeader("Authorization") String token, @RequestParam("placeId") String placeId, @RequestParam("offset") String offset) {
        try {
            UserEntity userEntity = tokenProvider.vaildateAndGetUser(token.substring(7));
            PlaceEntity placeEntity = placeService.findPlaceEntity(placeId).orElse(null);
            Page<DiaryEntity> diaryEntities = diaryService.getFilteredDiaries(userEntity, placeEntity, Integer.parseInt(offset));
            List<DiaryEntity> diaryList = diaryEntities.getContent();
            List<DiaryGetAllDTO> dtoList = new ArrayList<>();
            for (DiaryEntity diaryEntity : diaryList) {
                dtoList.add(DiaryGetAllDTO.toDTO(diaryEntity));
            }
            return ResponseEntity.ok(dtoList);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDiary(@RequestHeader("Authorization") String token, @PathVariable("id") String diaryId) {
        try {
            UserEntity userEntity = tokenProvider.vaildateAndGetUser(token.substring(7));

            DiaryEntity diaryEntity = diaryService.getByDiaryId(Long.parseLong(diaryId));
            if (!diaryEntity.getUser().getUserId().equals(userEntity.getUserId())) {
                // 둘이 동일하지 않은 사용자일 때 예외를 던집니다.
                throw new Exception("일기를 작성한 사용자와 요청한 사용자가 동일하지 않습니다.");
            }
            DiaryGetAllDTO dto = DiaryGetAllDTO.toDTO(diaryEntity);
            return ResponseEntity.ok(dto);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiary(@PathVariable("id") String diaryId) {
        try {
            diaryService.deleteByDiaryId(Long.parseLong(diaryId));
            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchDiary(@PathVariable("id") String diaryId, @RequestBody DiaryUpdateDTO dto) {
        try {
            diaryService.updateDiary(Long.parseLong(diaryId), dto);
            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
