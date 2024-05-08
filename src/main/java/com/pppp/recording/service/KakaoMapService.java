package com.pppp.recording.service;

import com.pppp.recording.dto.KakaoMapDTO;
import com.pppp.recording.dto.PlaceListDTO;
import com.pppp.recording.model.PlaceEntity;
import com.pppp.recording.repository.PlaceRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.swing.text.Document;

@Getter
@Setter
@Service
public class KakaoMapService {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    private final RestTemplate restTemplate;

    public KakaoMapService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public PlaceEntity findPlace(String placeName, String x, String y) {

        String apiUrl = "https://dapi.kakao.com/v2/local/search/keyword.json?query="+placeName+"&x="+x+"&y="+y;


        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + kakaoApiKey);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");

        ResponseEntity<PlaceListDTO> response = restTemplate.exchange(apiUrl, HttpMethod.GET,new HttpEntity<>(headers),PlaceListDTO.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            PlaceListDTO responseBody = response.getBody();
            if (responseBody.getDocuments() != null && !responseBody.getDocuments().isEmpty()) {

                // 여러 장소 중 첫 번째 장소를 선택하거나, 필요에 따라 로직을 추가하여 적절한 장소를 선택
                KakaoMapDTO firstPlace = responseBody.getDocuments().get(0);

                System.out.println(firstPlace);

                return new PlaceEntity(firstPlace.getId(), firstPlace.getPlace_name(), firstPlace.getX(), firstPlace.getY(), firstPlace.getPhone(), firstPlace.getPlace_url());
            }
        }
        return null; // 장소를 찾지 못한 경우
    }
}
