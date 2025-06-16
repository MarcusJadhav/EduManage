package com.example.edumanage.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "students")
@Data public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}