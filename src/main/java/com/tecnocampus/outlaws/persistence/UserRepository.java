package com.tecnocampus.outlaws.persistence;

import com.tecnocampus.outlaws.domain.Outlaw;
import com.tecnocampus.outlaws.domain.OutlawStatus;
import com.tecnocampus.outlaws.domain.Sheriff;
import com.tecnocampus.outlaws.domain.User;
import com.tecnocampus.outlaws.utilities.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public interface UserRepository {

    public List<User> getAllUsers();

    public List<Outlaw> getAllOutlaws();

    public List<Sheriff> getAllSheriffs();

    public User getUserById(String id);

    public Outlaw getOutlawById(String id);

    public Sheriff getSheriffById(String id);

    public void addUser(User user);

    public void removeUser(String id);

    public void removeAll();

    public Outlaw getMostWantedOutlaw();

    void updateOutlaw(Outlaw outlaw);

    void updateSheriff(Sheriff sheriff);
}
