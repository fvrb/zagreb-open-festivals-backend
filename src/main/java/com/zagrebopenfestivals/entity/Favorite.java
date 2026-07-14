package com.zagrebopenfestivals.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Explicitna "join" tablica između User-a i Festivala (umjesto @ManyToMany napravljeno
 * kao zaseban entitet) - ovako se lakše kasnije doda npr. createdAt na favorit.
 *
 * TODO (studentski zadatak): FavoriteService i FavoriteController još nisu implementirani.
 * Repozitorij i entitet su gotovi - nadogradite sloj iznad njih.
 */
@Entity
@Table(
        name = "favorites",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "festival_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id", nullable = false)
    private Festival festival;
}
