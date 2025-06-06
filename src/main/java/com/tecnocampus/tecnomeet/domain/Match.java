package com.tecnocampus.tecnomeet.domain;

import com.tecnocampus.outlaws.utilities.InvalidDataException;
import java.util.UUID;

public class Match {
    public enum Status {PENDING, MATCHED, UNMATCHED}

    private String id = UUID.randomUUID().toString();
    private String likerId;
    private String likedId;
    private Status status = Status.PENDING;

    public Match() {}

    public Match(String likerId, String likedId) {
        if (likerId == null || likedId == null) {
            throw new InvalidDataException("Users required");
        }
        this.likerId = likerId;
        this.likedId = likedId;
    }

    public String getId() { return id; }
    public String getLikerId() { return likerId; }
    public String getLikedId() { return likedId; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
