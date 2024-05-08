package com.pppp.recording.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
@Getter
@Builder
public class DiaryUpdateDTO {
    private LocalDate visitDate;
    private String content;
    private Integer rating;
    private Boolean goPublic;
    private String weather;
}

