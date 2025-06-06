package com.tecnocampus.outlaws.domain;

import com.tecnocampus.outlaws.application.dto.SheriffDTO;
import com.tecnocampus.outlaws.utilities.InvalidDataException;
import com.tecnocampus.outlaws.utilities.InvalidStateChangeException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Sheriff extends User {
    private int salary;
    private int captures;
    private int eliminations;

    public Sheriff() {
        super();
    }

    public Sheriff(SheriffDTO sheriffDTO) {
        super(sheriffDTO.name());

        if (sheriffDTO.salary() < 0) {
            throw new InvalidDataException("Salary must be a positive number");
        }

        if (sheriffDTO.captures() < 0) {
            throw new InvalidDataException("Salary must be a positive number");
        }
        if (sheriffDTO.eliminations() < 0) {
            throw new InvalidDataException("Salary must be a positive number");
        }

        this.salary = sheriffDTO.salary();
        this.captures = sheriffDTO.captures();
        this.eliminations = sheriffDTO.eliminations();
    }

    public Sheriff(ResultSet rs) throws SQLException {
        super(rs);
        salary = rs.getInt("salary");
        captures = rs.getInt("captures");
        eliminations = rs.getInt("eliminations");
    }

    public int getSalary() {
        return salary;
    }

    public int getCaptures() {
        return captures;
    }

    public int getEliminations() {
        return eliminations;
    }

    public void captureOutlaw(Outlaw outlaw) {
        if (outlaw.getStatus() != OutlawStatus.FREE) {
            throw new InvalidStateChangeException("Only free outlaws can be captured");
        }
        outlaw.changeStatus(OutlawStatus.CAPTURED);
        this.salary += outlaw.getBounty();
        this.captures++;
    }

    public void eliminateOutlaw(Outlaw outlaw) {
        if (outlaw.getStatus() == OutlawStatus.ELIMINATED) {
            throw new InvalidStateChangeException("Outlaw is already eliminated");
        }
        outlaw.changeStatus(OutlawStatus.ELIMINATED);
        this.eliminations++;
    }


    public void setSalary(int amount) {
        if (amount < 0) {
            throw new InvalidDataException("Salary increase cannot be negative");
        }
        this.salary = amount;
    }

    public void setEliminations(int eliminations) {
        if (eliminations < 0) {
            throw new InvalidDataException("Salary increase cannot be negative");
        }
        this.eliminations = eliminations;
    }

    public void setCaptures(int captures) {
        if (captures < 0) {
            throw new InvalidDataException("Salary increase cannot be negative");
        }
        this.captures = captures;
    }

    public void update(SheriffDTO sheriffToUpdate) {
        if (sheriffToUpdate == null) {
            throw new InvalidDataException("Salary increase cannot be negative");
        }
        setSalary(sheriffToUpdate.salary());
        setName(sheriffToUpdate.name());
        setEliminations(sheriffToUpdate.eliminations());
        setCaptures(sheriffToUpdate.captures());
    }

    public SheriffDTO toDTO() {
        return new SheriffDTO(id, createdAt, name, salary, captures, eliminations);
    }
}
