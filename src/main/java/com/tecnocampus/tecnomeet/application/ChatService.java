package com.tecnocampus.tecnomeet.application;

import com.tecnocampus.tecnomeet.application.dto.ChatMessageDTO;
import com.tecnocampus.tecnomeet.domain.ChatMessage;
import com.tecnocampus.tecnomeet.domain.Match;
import com.tecnocampus.tecnomeet.persistence.ChatMessageRepository;
import com.tecnocampus.tecnomeet.persistence.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    private final ChatMessageRepository messageRepository;
    private final MatchRepository matchRepository;

    public ChatService(ChatMessageRepository messageRepository, MatchRepository matchRepository) {
        this.messageRepository = messageRepository;
        this.matchRepository = matchRepository;
    }

    public ChatMessageDTO sendMessage(String matchId, String senderId, String content) {
        Match m = matchRepository.getMatchById(matchId);
        if (m.getStatus() != Match.Status.MATCHED) {
            throw new RuntimeException("Match not active");
        }
        ChatMessage msg = new ChatMessage(matchId, senderId, content);
        messageRepository.addMessage(msg);
        return toDTO(msg);
    }

    public List<ChatMessageDTO> getMessages(String matchId) {
        return messageRepository.getMessagesByMatch(matchId).stream().map(this::toDTO).toList();
    }

    private ChatMessageDTO toDTO(ChatMessage m) {
        return new ChatMessageDTO(m.getId(), m.getMatchId(), m.getSenderId(), m.getContent(), m.getCreatedAt().toString());
    }
}
