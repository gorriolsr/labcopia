package com.tecnocampus.tecnomeet.domain;

import com.tecnocampus.outlaws.utilities.InvalidDataException;
import java.time.LocalDateTime;
import java.util.UUID;

public class ChatMessage {
    private String id = UUID.randomUUID().toString();
    private String matchId;
    private String senderId;
    private String content;
    private LocalDateTime createdAt = LocalDateTime.now();

    public ChatMessage() {}

    public ChatMessage(String matchId, String senderId, String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidDataException("Message content required");
        }
        this.matchId = matchId;
        this.senderId = senderId;
        this.content = content;
    }

    public String getId() { return id; }
    public String getMatchId() { return matchId; }
    public String getSenderId() { return senderId; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
