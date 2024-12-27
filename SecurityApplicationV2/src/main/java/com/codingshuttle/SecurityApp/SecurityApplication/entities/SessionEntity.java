package com.codingshuttle.SecurityApp.SecurityApplication.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="sessions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refreshToken;


    @CreationTimestamp
    private LocalDateTime lastusedAt;

   @ManyToOne
    private User user;




   /* public SessionEntity(Long userId, String token) {
        this.userId = userId;
        this.token = token;
        this.createdAt = LocalDateTime.now();
    }*/









}
