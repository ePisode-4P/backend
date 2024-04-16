package com.pppp.recording.model;
import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Favorites")
public class FavoriteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private CategoryEntity favorite;

    public FavoriteEntity(UserEntity user, CategoryEntity category) {
        this.user = user;
        this.favorite = category;
    }

}
