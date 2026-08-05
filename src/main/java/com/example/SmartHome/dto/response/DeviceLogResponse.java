package com.example.SmartHome.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceLogResponse {
    private Long id;
    private Long deviceId;
    private String eventType;
    private String oldValue;
    private String newValue;
    private LocalDateTime timestamp;
}
