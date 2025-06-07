package com.tecnocampus.tecnomeet.persistence;

import com.tecnocampus.tecnomeet.domain.Match;
import com.tecnocampus.tecnomeet.domain.Match.Status;
import com.tecnocampus.outlaws.utilities.NotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Profile("db-h2")
public class DBMatchRepository implements MatchRepository {
    private final JdbcClient jdbcClient;

    public DBMatchRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private Match mapMatch(ResultSet rs) throws SQLException {
        Match m = new Match(rs.getString("liker_id"), rs.getString("liked_id"));
        m.setStatus(Status.valueOf(rs.getString("status")));
        // set id
        try {
            java.lang.reflect.Field idField = Match.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(m, rs.getString("id"));
        } catch (Exception ignored) {}
        return m;
    }

    @Override
    public void addMatch(Match match) {
        jdbcClient.sql("INSERT INTO MATCHES (id,liker_id,liked_id,status) VALUES (:id,:liker,:liked,:status)")
                .param("id", match.getId())
                .param("liker", match.getLikerId())
                .param("liked", match.getLikedId())
                .param("status", match.getStatus().name())
                .update();
    }

    @Override
    public Match getMatchById(String id) {
        return jdbcClient.sql("SELECT * FROM MATCHES WHERE id=:id")
                .param("id", id)
                .query((rs, rowNum) -> mapMatch(rs)).stream().findFirst()
                .orElseThrow(() -> new NotFoundException("Match not found"));
    }

    @Override
    public Match findPendingMatch(String likerId, String likedId) {
        return jdbcClient.sql("SELECT * FROM MATCHES WHERE liker_id=:l2 AND liked_id=:l1 AND status='PENDING'")
                .param("l2", likedId)
                .param("l1", likerId)
                .query((rs, rowNum) -> mapMatch(rs)).stream().findFirst().orElse(null);
    }

    @Override
    public void updateMatch(Match match) {
        int rows = jdbcClient.sql("UPDATE MATCHES SET status=:status WHERE id=:id")
                .param("status", match.getStatus().name())
                .param("id", match.getId()).update();
        if (rows==0) throw new NotFoundException("Match not found");
    }

    @Override
    public List<Match> getMatchesByUser(String userId) {
        return jdbcClient.sql("SELECT * FROM MATCHES WHERE (liker_id=:id OR liked_id=:id) AND status='MATCHED'")
                .param("id", userId)
                .query((rs, rowNum) -> mapMatch(rs)).list();
    }
}
