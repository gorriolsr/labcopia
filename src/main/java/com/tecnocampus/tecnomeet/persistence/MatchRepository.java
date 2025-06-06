package com.tecnocampus.tecnomeet.persistence;

import com.tecnocampus.tecnomeet.domain.Match;

import java.util.List;

public interface MatchRepository {
    void addMatch(Match match);
    Match getMatchById(String id);
    Match findPendingMatch(String likerId, String likedId);
    void updateMatch(Match match);
    List<Match> getMatchesByUser(String userId);
}
