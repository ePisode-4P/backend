package com.pppp.recording.dto;

import com.pppp.recording.model.DiaryEntity;
import com.pppp.recording.model.PlaceEntity;
import com.pppp.recording.model.UserEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Builder
public class DiaryGetAllDTO {
    private Long diaryId;
    private LocalDateTime writeDate;
    private String placeId;
    private Boolean goPublic;
    private Integer rating;
    private LocalDate visitDate;
    private String content;
    private List<String> image;
    private String weather;
    public DiaryGetAllDTO(Long diaryId, LocalDateTime writeDate, String placeId, Boolean goPublic, Integer rating,LocalDate visitDate, String content, List<String> image, String weather) {
        this.diaryId = diaryId;
        this.writeDate = writeDate;
        this.placeId = placeId;
        this.goPublic = goPublic;
        this.rating = rating;
        this.visitDate = visitDate;
        this.content = content;
        this.image = image;
        this.weather = weather;
    }

    public static DiaryGetAllDTO toDTO(DiaryEntity entity) {
        return DiaryGetAllDTO.builder()
                .diaryId(entity.getDiaryId())
                .writeDate(entity.getWriteDate())
                .placeId(entity.getPlace().getPlaceId())
                .goPublic(entity.getGoPublic())
                .goPublic(entity.getGoPublic())
                .rating(entity.getRating())
                .visitDate(entity.getVisitDate())
                .content(entity.getContent())
                .weather(entity.getWeather())
                .build();
    }
}