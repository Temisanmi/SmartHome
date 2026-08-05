package com.example.SmartHome.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DeviceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device_log_seq")
    @SequenceGenerator(name= "device_log_seq", sequenceName = "device_log_seq", allocationSize = 20)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "device_id")
    @JsonIgnore
    private Device device;

    private String eventType;

    private String oldValue;

    private String newValue;

    private LocalDateTime timestamp = LocalDateTime.now();
}
