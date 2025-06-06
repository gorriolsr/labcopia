package com.tecnocampus.tecnomeet.application.dto;

public record ChatMessageDTO(String id, String matchId, String senderId, String content, String createdAt) {}
