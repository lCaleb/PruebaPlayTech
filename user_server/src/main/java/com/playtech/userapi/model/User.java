package com.playtech.userapi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_birth", nullable = false)
    private LocalDate dateBirth;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "token", nullable = true)
    private String token;

    @Column(name = "password", nullable = false, length = 120)
    private String password;

    @Column(name = "mobile_phone", nullable = false)
    private String mobilePhone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @CreationTimestamp
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    private LocalDateTime actualizadoEn;
}
