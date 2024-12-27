package com.codingshuttle.SecurityApp.SecurityApplication.entities;

import com.codingshuttle.SecurityApp.SecurityApplication.entities.enums.PlanName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PlanName planName;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
