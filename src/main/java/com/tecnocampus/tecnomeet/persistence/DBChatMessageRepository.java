package com.tecnocampus.tecnomeet.persistence;

import com.tecnocampus.tecnomeet.domain.ChatMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@Profile("db-h2")
public class DBChatMessageRepository implements ChatMessageRepository {
    private final JdbcClient jdbcClient;

    public DBChatMessageRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void addMessage(ChatMessage message) {
        jdbcClient.sql("INSERT INTO CHAT_MESSAGES (id,match_id,sender_id,content,created_at) VALUES (:id,:matchId,:senderId,:content,:createdAt)")
                .param("id", message.getId())
                .param("matchId", message.getMatchId())
                .param("senderId", message.getSenderId())
                .param("content", message.getContent())
                .param("createdAt", Timestamp.valueOf(message.getCreatedAt()))
                .update();
    }

    @Override
    public List<ChatMessage> getMessagesByMatch(String matchId) {
        return jdbcClient.sql("SELECT * FROM CHAT_MESSAGES WHERE match_id=:id ORDER BY created_at")
                .param("id", matchId)
                .query((rs, rowNum) -> {
                    ChatMessage m = new ChatMessage(rs.getString("match_id"), rs.getString("sender_id"), rs.getString("content"));
                    try {
                        java.lang.reflect.Field idField = ChatMessage.class.getDeclaredField("id");
                        idField.setAccessible(true);
                        idField.set(m, rs.getString("id"));
                        java.lang.reflect.Field createdField = ChatMessage.class.getDeclaredField("createdAt");
                        createdField.setAccessible(true);
                        createdField.set(m, rs.getTimestamp("created_at").toLocalDateTime());
                    } catch (Exception ignored) {}
                    return m;
                }).list();
    }
}
