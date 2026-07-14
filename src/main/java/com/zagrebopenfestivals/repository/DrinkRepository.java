package com.zagrebopenfestivals.repository;

import com.zagrebopenfestivals.entity.Drink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repozitorij je gotov - servis i controller za Drink su TODO (studentski zadatak),
 * po uzoru na FoodRepository/FoodService/FoodController.
 */
@Repository
public interface DrinkRepository extends JpaRepository<Drink, Long> {

    List<Drink> findAllByFestivalId(Long festivalId);
}
