package com.Spring.EducaPro.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título da aula é obrigatório")
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "duration_minutes")
    private Integer durationMinutes; // Duração em minutos

    @Column(name = "lesson_order")
    private Integer lessonOrder; // Ordem da aula no módulo/curso

    @Column(name = "module_name")
    private String moduleName; // Nome do módulo (ex: "Módulo 1: Fundamentos")

    @NotNull(message = "Curso é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "is_completed")
    private Boolean isCompleted = false; // Para rastrear progresso do aluno
}

