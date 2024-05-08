package com.pppp.recording.service;

import com.pppp.recording.dto.DiaryDTO;
import com.pppp.recording.dto.DiaryUpdateDTO;
import com.pppp.recording.model.DiaryEntity;
import com.pppp.recording.model.PlaceEntity;
import com.pppp.recording.model.UserEntity;
import com.pppp.recording.repository.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
@Service
@ToString
public class DiaryService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final DiaryImageRepository diaryImageRepository;
    private final DiariesRepository diariesRepository;

    @Transactional
    public void save(DiaryEntity diaryEntity){
        diariesRepository.save(diaryEntity);
    }


    public Page<DiaryEntity> getFilteredDiaries(UserEntity user, PlaceEntity placeId, Integer offset) {
        Pageable pageable = PageRequest.of(offset, 15);
        return diariesRepository.findByUserAndPlace(user, placeId, pageable);
    }

    public DiaryEntity getByDiaryId(Long diaryId) {
        return diariesRepository.findByDiaryId(diaryId);
    }

    public void deleteByDiaryId(Long diaryId) {
        diariesRepository.deleteByDiaryId(diaryId);
    }
    public void updateDiary(Long diaryId, DiaryUpdateDTO updateDiaryDTO) throws Exception {
        // 1. 업데이트할 다이어리의 정보를 가져옵니다.
        Optional<DiaryEntity> optionalDiary = diariesRepository.findById(diaryId);
        if (optionalDiary.isEmpty()) {
            throw new InstanceNotFoundException("Diary not found with ID: " + diaryId);
        }

        // 2. 업데이트할 필드 값을 DTO에서 가져와 엔티티에 설정합니다.
        DiaryEntity diaryEntity = optionalDiary.get();
        diaryEntity.setVisitDate(updateDiaryDTO.getVisitDate());
        diaryEntity.setContent(updateDiaryDTO.getContent());
        diaryEntity.setGoPublic(updateDiaryDTO.getGoPublic());
        diaryEntity.setRating(updateDiaryDTO.getRating());
        diaryEntity.setWeather(updateDiaryDTO.getWeather());

        // 3. 엔티티를 저장하여 업데이트합니다.
        diariesRepository.save(diaryEntity);
    }
    //장소 ID 찾고 없으면 추가 함

    // 다이어리 추가 삭제 조회 수정
}
