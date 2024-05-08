package com.pppp.recording.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Diary_image")
public class DiaryImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diaryImageId;

    @ManyToOne
    @JoinColumn(name = "diaryId")
    private DiaryEntity diary;

    private String imageUrl;

    public DiaryImageEntity(DiaryEntity diary, String imageUrl) {
        this.diary = diary;
        this.imageUrl = imageUrl;
    }

}

