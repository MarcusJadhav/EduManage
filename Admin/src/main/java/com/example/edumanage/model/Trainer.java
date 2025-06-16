package com.example.edumanage.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "trainers")
@Data
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}