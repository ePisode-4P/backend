package com.pppp.recording.repository;

import com.pppp.recording.model.PlaceEntity;
import com.pppp.recording.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<PlaceEntity, String> {
    Optional<PlaceEntity> findByPlaceId (String placeId);
}
