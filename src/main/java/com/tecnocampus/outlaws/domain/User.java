package com.tecnocampus.outlaws.domain;

import com.tecnocampus.outlaws.application.dto.OutlawDTO;
import com.tecnocampus.outlaws.utilities.InvalidDataException;
import jakarta.validation.constraints.NotBlank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public abstract class User {
    protected String id = UUID.randomUUID().toString();
    @NotBlank
    protected String name;
    protected boolean deleted = false;
    protected LocalDateTime createdAt = LocalDateTime.now();
    protected String type = getClass().getSimpleName();

    public User() {

    }

    public User(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidDataException("Name is required");
        }
        this.name = name;
    }

    public User(ResultSet rs) throws SQLException {
        id = rs.getString("id");
        name = rs.getString("name");
        deleted = rs.getBoolean("deleted");
        createdAt = Optional.of(rs.getTimestamp("created_at"))
                .map(Timestamp::toLocalDateTime).get();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidDataException("Name is required");
        }
        this.name = name;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getType() {
        return type;
    }
}
