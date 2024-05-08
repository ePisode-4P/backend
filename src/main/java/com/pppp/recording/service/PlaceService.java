package com.pppp.recording.service;

import com.pppp.recording.model.PlaceEntity;
import com.pppp.recording.repository.PlaceRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Getter
@Setter
@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    public String savePlace(PlaceEntity placeEntity) {
        return placeRepository.save(placeEntity).getPlaceId();
    }
    public Optional<PlaceEntity> findPlaceEntity (String placeId) {
        return placeRepository.findByPlaceId(placeId);
    }

}
