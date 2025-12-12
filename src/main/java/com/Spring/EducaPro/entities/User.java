package com.Spring.EducaPro.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Telefone é obrigatório")
    @Column(nullable = false)
    private String phone;

    @NotBlank(message = "Nível de educação é obrigatório")
    @Column(name = "education_level", nullable = false)
    private String educationLevel;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail deve ser válido")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @NotBlank(message = "Endereço é obrigatório")
    @Column(nullable = false)
    private String address;

    @NotBlank(message = "Objetivo é obrigatório")
    @Column(nullable = false)
    private String objective;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    @Column(nullable = false)
    private String password;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }

    // Método auxiliar para converter String dd/mm/yyyy para LocalDate
    public void setBirthDateFromString(String dateString) {
        if (dateString != null && !dateString.isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                this.birthDate = LocalDate.parse(dateString, formatter);
            } catch (Exception e) {
                throw new IllegalArgumentException("Data inválida. Use o formato dd/mm/yyyy");
            }
        }
    }
}

