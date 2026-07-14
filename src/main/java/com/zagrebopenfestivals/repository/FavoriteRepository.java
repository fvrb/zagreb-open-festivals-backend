package com.zagrebopenfestivals.repository;

import com.zagrebopenfestivals.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repozitorij je gotov - FavoriteService/FavoriteController su TODO (studentski zadatak).
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByUserId(Long userId);

    Optional<Favorite> findByUserIdAndFestivalId(Long userId, Long festivalId);

    boolean existsByUserIdAndFestivalId(Long userId, Long festivalId);
}
