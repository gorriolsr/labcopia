package com.tecnocampus.tecnomeet.persistence;

import com.tecnocampus.tecnomeet.domain.ChatMessage;
import java.util.List;

public interface ChatMessageRepository {
    void addMessage(ChatMessage message);
    List<ChatMessage> getMessagesByMatch(String matchId);
}
