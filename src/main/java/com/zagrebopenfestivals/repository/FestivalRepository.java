package com.zagrebopenfestivals.repository;

import com.zagrebopenfestivals.entity.Festival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FestivalRepository extends JpaRepository<Festival, Long> {

    List<Festival> findByNameContainingIgnoreCase(String name);
}
