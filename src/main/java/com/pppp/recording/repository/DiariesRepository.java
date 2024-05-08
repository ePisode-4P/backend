package com.pppp.recording.repository;
import com.pppp.recording.model.PlaceEntity;
import org.springframework.data.domain.Page;
import com.pppp.recording.model.DiaryEntity;
import com.pppp.recording.model.FavoriteEntity;
import com.pppp.recording.model.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiariesRepository extends JpaRepository<DiaryEntity, Long> {
    Page<DiaryEntity> findByUserAndPlace(UserEntity user, PlaceEntity place, Pageable pageable);

    DiaryEntity findByDiaryId(Long diaryId);

    void deleteByDiaryId(Long diaryId);
}