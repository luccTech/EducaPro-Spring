package com.Spring.EducaPro.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título é obrigatório")
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Categoria é obrigatória")
    @Column(nullable = false)
    private String category; // Ex: programming, design, marketing, etc.

    @Column(name = "education_level")
    private String educationLevel; // Ex: beginner, intermediate, advanced

    @Column(name = "duration_hours")
    private Integer durationHours; // Duração em horas

    @Column(precision = 3, scale = 2)
    private BigDecimal rating; // Avaliação de 0 a 5

    @Column(name = "rating_count")
    private Integer ratingCount; // Número de avaliações

    @Column(name = "instructor_name")
    private String instructorName;

    @Column(name = "instructor_id")
    private Long instructorId; // ID do usuário que é instrutor

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "icon_class")
    private String iconClass; // Classe do ícone (ex: "ph ph-brain")

    @Column(name = "created_at")
    private LocalDate createdAt;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        if (rating == null) {
            rating = BigDecimal.ZERO;
        }
        if (ratingCount == null) {
            ratingCount = 0;
        }
    }
}

