package com.pppp.recording.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class PlaceListDTO {
    private List<KakaoMapDTO> documents;
    public List<KakaoMapDTO> getPlaces() {
        return documents;
    }

    public void setPlaces(List<KakaoMapDTO> places) {
        this.documents = places;
    }
}
