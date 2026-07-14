package com.zagrebopenfestivals.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "festivals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Festival {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false, length = 150)
    private String location;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "image_url")
    private String imageUrl;

    // Lazy by default za @OneToMany - festival.getFoods()/getDrinks() smiju se
    // pozivati SAMO dok je Hibernate session (transakcija) još otvorena!
    @Builder.Default
    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Food> foods = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Drink> drinks = new ArrayList<>();
}
