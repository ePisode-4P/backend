package com.pppp.recording.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Diaries")
public class DiaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diaryId;

    @Column(nullable = false)
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm:ss")
    private LocalDateTime writeDate;

//    @Column(nullable = false)
    private LocalDate visitDate;

//    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean goPublic;

//    @Column(nullable = false)
    private Integer rating;

//    @Column(nullable = false)
    private String weather;


    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "placeId")
    private PlaceEntity place;

    public DiaryEntity(UserEntity user, PlaceEntity place) {
        this.user = user;
        this.place = place;
    }
}

