package com.pppp.recording.dto;

import com.pppp.recording.model.DiaryEntity;
import com.pppp.recording.model.PlaceEntity;
import com.pppp.recording.model.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
public class DiaryDTO {

    @NotBlank(message = "장소 ID를 입력해주세요")
    private String placeId;

    @NotBlank(message = "장소이름을 입력해주세요")
    private String placeName;

    @NotBlank(message = "위도를 입력해주세요")
    private String x;

    @NotBlank(message = "경도를 입력해주세요.")
    private String y;

    @NotBlank(message = "공개여부를 입력해주세요.")
    private Boolean goPublic;

    private Integer rating;
    private LocalDate visitDate;
    private String content;
    private List<String> image;
    private String weather;
    public DiaryEntity toDiaryEntity(UserEntity user, PlaceEntity place, LocalDateTime dateTime) {
        return DiaryEntity.builder().user(user).place(place).goPublic(goPublic).rating(rating).visitDate(visitDate).content(content).weather(weather).writeDate(dateTime).build();
    }
}
