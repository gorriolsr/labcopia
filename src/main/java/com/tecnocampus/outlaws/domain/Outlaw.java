package com.tecnocampus.outlaws.domain;

import com.tecnocampus.outlaws.application.dto.OutlawDTO;
import com.tecnocampus.outlaws.utilities.InvalidDataException;
import com.tecnocampus.outlaws.utilities.InvalidStateChangeException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Outlaw extends User {
    private int bounty;
    private OutlawStatus status;

    public Outlaw() {

    }

    public Outlaw(OutlawDTO outlawDTO) {
        super(outlawDTO.name());
        if (outlawDTO.bounty() < 0) {
            throw new InvalidDataException("Bounty must be a positive number");
        }

        this.bounty = outlawDTO.bounty();
        if (outlawDTO.status() != null && !outlawDTO.status().isBlank())
            this.status = OutlawStatus.valueOf(outlawDTO.status().toUpperCase());
        else this.status = OutlawStatus.FREE;
    }


    public Outlaw(ResultSet rs) throws SQLException {
        super(rs);
        this.bounty = rs.getInt("bounty");
        this.status = OutlawStatus.valueOf(rs.getString("status"));
    }


    public int getBounty() {
        return bounty;
    }

    public OutlawStatus getStatus() {
        return status;
    }

    public void setBounty(int amount) {
        if (amount < 0) {
            throw new InvalidDataException("Bounty increase cannot be negative");
        }
        this.bounty = amount;
    }

    public void changeStatus(OutlawStatus newStatus) {
        validateStatusChange(newStatus);
        this.status = newStatus;
    }

    private void validateStatusChange(OutlawStatus newStatus) {
        if (this.status == OutlawStatus.ELIMINATED) {
            throw new InvalidStateChangeException("An eliminated outlaw's status cannot be changed");
        }
        if (this.status == OutlawStatus.CAPTURED && newStatus == OutlawStatus.CAPTURED) {
            throw new InvalidStateChangeException("Outlaw is already captured");
        }
        if (this.status == OutlawStatus.FREE && !isValidFreeStatusChange(newStatus)) {
            throw new InvalidStateChangeException("Invalid status change from free");
        }
        if (this.status == OutlawStatus.CAPTURED && !isValidCapturedStatusChange(newStatus)) {
            throw new InvalidStateChangeException("Invalid status change from captured");
        }
    }

    private boolean isValidFreeStatusChange(OutlawStatus newStatus) {
        return newStatus == OutlawStatus.CAPTURED || newStatus == OutlawStatus.ELIMINATED;
    }

    private boolean isValidCapturedStatusChange(OutlawStatus newStatus) {
        return newStatus == OutlawStatus.ELIMINATED || newStatus == OutlawStatus.FREE;
    }

    public void update(OutlawDTO outlawDTO) {
        if (outlawDTO.bounty() < 0) {
            throw new InvalidDataException("Bounty must be a positive number");
        }
        this.bounty = outlawDTO.bounty();
        setName(outlawDTO.name());
    }

    public OutlawDTO toDTO() {
        return new OutlawDTO(id, createdAt, name, bounty, status.name());
    }
}
