package com.example.SmartHome.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Home {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "home_seq")
    @SequenceGenerator(name = "home_seq", sequenceName = "home_seq", allocationSize = 20)
    private Long id;

    private String name;

    private String address;

    private Double tariffPerKwh;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();
}
