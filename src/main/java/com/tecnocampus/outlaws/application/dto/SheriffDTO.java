package com.tecnocampus.outlaws.application.dto;

import java.time.LocalDateTime;

public record SheriffDTO(String id, LocalDateTime createdAt, String name, int salary, int captures, int eliminations) {
}
