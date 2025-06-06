package com.tecnocampus.outlaws.persistence;

import com.tecnocampus.outlaws.domain.Outlaw;
import com.tecnocampus.outlaws.domain.OutlawStatus;
import com.tecnocampus.outlaws.domain.Sheriff;
import com.tecnocampus.outlaws.domain.User;
import com.tecnocampus.outlaws.utilities.NotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
@Profile("in-memory")
public class InMemoryUserRepository implements UserRepository {
    private static final List<User> users = new ArrayList<>();

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public List<Outlaw> getAllOutlaws() {
        return getAllUsers().stream()
                .filter(Outlaw.class::isInstance)
                .filter(user -> !user.isDeleted())
                .map(Outlaw.class::cast)
                .toList();
    }

    public List<Sheriff> getAllSheriffs() {
        return getAllUsers().stream()
                .filter(Sheriff.class::isInstance)
                .filter(user -> !user.isDeleted())
                .map(Sheriff.class::cast)
                .toList();
    }

    public User getUserById(String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id) && !user.isDeleted())
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public Outlaw getOutlawById(String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id) && !user.isDeleted())
                .filter(Outlaw.class::isInstance)
                .map(Outlaw.class::cast)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User not found"));
    }


    public Sheriff getSheriffById(String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id) && !user.isDeleted())
                .filter(Sheriff.class::isInstance)
                .map(Sheriff.class::cast)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(String id) {
        User user = getUserById(id);
        user.setDeleted(true);
    }

    public void removeAll() {
        users.clear();
    }

    public Outlaw getMostWantedOutlaw() {
        return getAllOutlaws().stream()
                .filter(outlaw -> outlaw.getStatus() == OutlawStatus.FREE)
                .sorted(Comparator.comparing(Outlaw::getBounty).reversed() // Highest bounty first
                        .thenComparing(Outlaw::getCreatedAt)).findFirst().orElseThrow(NotFoundException::new);

    }

    @Override
    public void updateOutlaw(Outlaw outlaw) {

    }

    @Override
    public void updateSheriff(Sheriff sheriff) {

    }
}
