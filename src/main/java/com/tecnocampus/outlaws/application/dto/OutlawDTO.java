package com.tecnocampus.outlaws.application.dto;

import java.time.LocalDateTime;

public record OutlawDTO(String id, LocalDateTime createdAt , String name, int bounty, String status) {
}
