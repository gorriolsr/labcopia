package com.tecnocampus.outlaws.persistence;

import com.tecnocampus.outlaws.domain.Outlaw;
import com.tecnocampus.outlaws.domain.OutlawStatus;
import com.tecnocampus.outlaws.domain.Sheriff;
import com.tecnocampus.outlaws.domain.User;
import com.tecnocampus.outlaws.utilities.NotFoundException;
import org.springframework.context.annotation.Profile;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


@Repository
@Profile("db-h2")
public class DBUserRepository implements UserRepository {

    private final JdbcClient jdbcClient;

    public DBUserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private User mapUser(ResultSet rs) throws SQLException {

        String type = rs.getString("user_type");
        if (Outlaw.class.getSimpleName().equalsIgnoreCase(type)) {
            return new Outlaw(rs);
        } else if (Sheriff.class.getSimpleName().equalsIgnoreCase(type)) {
            return new Sheriff(rs);
        } else throw new NotFoundException("Type user not found");
    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM USERS";
        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> mapUser(rs)).list();

    }

    public List<Outlaw> getAllOutlaws() {
        return jdbcClient.sql("SELECT * FROM USERS WHERE user_type = :user_type")
                .param("user_type", Outlaw.class.getSimpleName())
                .query((rs, rowNum) -> mapUser(rs)).list()
                .stream().map(Outlaw.class::cast).toList();
    }

    public List<Sheriff> getAllSheriffs() {
        return jdbcClient.sql("SELECT * FROM USERS WHERE user_type = :type")
                .param("type", Sheriff.class.getSimpleName())
                .query((rs, rowNum) -> mapUser(rs)).list()
                .stream().map(Sheriff.class::cast).toList();
    }

    public User getUserById(String id) {
        return jdbcClient.sql("SELECT * FROM USERS WHERE id = :id")
                .param("id", id)
                .query((rs, rowNum) -> mapUser(rs)).optional()
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));
    }

    public Outlaw getOutlawById(String id) {
        return jdbcClient.sql("SELECT * FROM USERS WHERE id = :id AND user_type = :type")
                .param("id", id)
                .param("type", Outlaw.class.getSimpleName())
                .query((rs, rowNum) -> mapUser(rs))
                .stream().map(Outlaw.class::cast).findAny()
                .orElseThrow(() -> new NotFoundException("Outlaw not found with id " + id));
    }

    public Sheriff getSheriffById(String id) {
        return jdbcClient.sql("SELECT * FROM USERS WHERE id = :id AND user_type = :type")
                .param("id", id)
                .param("type", Sheriff.class.getSimpleName())
                .query((rs, rowNum) -> mapUser(rs))
                .stream().map(Sheriff.class::cast).findAny()
                .orElseThrow(() -> new NotFoundException("Outlaw not found with id " + id));
    }


    @Override
    public void updateOutlaw(Outlaw outlaw) {

        int rows = jdbcClient
                .sql("""
                        UPDATE USERS
                           SET name        = :name,
                               deleted     = :deleted,                                                                                                                            
                               bounty      = :bounty,
                               status    = :status                             
                         WHERE id = :id
                        """)
                .param("id", outlaw.getId())
                .param("name", outlaw.getName())
                .param("deleted", outlaw.isDeleted())
                .param("type", outlaw.getType())
                .param("bounty", outlaw.getBounty())
                .param("status", outlaw.getStatus().name())
                .update();

        if (rows == 0) {
            throw new NotFoundException("User not found with id " + outlaw.getId());
        }
    }

    @Override
    public void updateSheriff(Sheriff sheriff) {

        int rows = jdbcClient
                .sql("""
                        UPDATE USERS
                           SET name        = :name,
                               deleted     = :deleted,                                                                                                                            
                               salary      = :salary,
                               captures    = :captures,
                               eliminations= :eliminations
                         WHERE id = :id
                        """)
                .param("id", sheriff.getId())
                .param("name", sheriff.getName())
                .param("deleted", sheriff.isDeleted())
                .param("salary", sheriff.getSalary())
                .param("captures", sheriff.getCaptures())
                .param("eliminations", sheriff.getEliminations())
                .update();

        if (rows == 0) {
            throw new NotFoundException("User not found with id " + sheriff.getId());
        }
    }

    public void addUser(User user) {
        var params = Map.<String, Object>of(
                "id", user.getId().toString(),
                "name", user.getName(),
                "deleted", user.isDeleted(),
                "createdAt", Timestamp.valueOf(user.getCreatedAt()),
                "type", user.getType(),
                "bounty", user instanceof Outlaw ? ((Outlaw) user).getBounty() : 0,
                "status", user instanceof Outlaw ? ((Outlaw) user).getStatus().name() : "",
                "salary", user instanceof Sheriff ? ((Sheriff) user).getSalary() : 0,
                "captures", user instanceof Sheriff ? ((Sheriff) user).getCaptures() : 0,
                "eliminations", user instanceof Sheriff ? ((Sheriff) user).getEliminations() : 0
        );

        jdbcClient.sql("INSERT INTO USERS (id, name, deleted, created_at, user_type, bounty, status, salary, captures, eliminations) " +
                        "VALUES (:id, :name, :deleted, :createdAt, :type, :bounty, :status, :salary, :captures, :eliminations)")
                .params(params)
                .update();
    }

    public void removeUser(String id) {
        int rows = jdbcClient.sql("DELETE FROM USERS WHERE id = :id")
                .param("id", id)
                .update();
        if (rows == 0) {
            throw new NotFoundException("User not found with id " + id);
        }
    }

    public void removeAll() {
        jdbcClient.sql("DELETE FROM USERS").update();
    }

    public Outlaw getMostWantedOutlaw() {
        return jdbcClient.sql("SELECT * FROM USERS WHERE user_type = :type AND status = :status " +
                        "ORDER BY bounty DESC, created_at ASC LIMIT 1")
                .param("type", Outlaw.class.getSimpleName())
                .param("status", OutlawStatus.FREE.name())
                .query((rs, rowNum) -> mapUser(rs))
                .stream().map(Outlaw.class::cast).findAny()
                .orElseThrow(() -> new NotFoundException("No free outlaws found"));
    }
}
