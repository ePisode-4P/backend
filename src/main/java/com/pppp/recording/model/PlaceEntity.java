package com.pppp.recording.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Places")
public class PlaceEntity {
    @Id
    @Column(nullable = false)
    private String placeId;

    @Column(nullable = false)
    private String placeName;

//    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

//    @Column(nullable = false)
    private String placeUrl;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private CategoryEntity category;

    public PlaceEntity(String id, String placeName, String x, String y, String phone, String placeUrl) {
        this.placeId = id;
        this.placeName = placeName;
        this.latitude = y;
        this.longitude = x;
        this.phone = phone;
        this.placeUrl = placeUrl;
    }
}
