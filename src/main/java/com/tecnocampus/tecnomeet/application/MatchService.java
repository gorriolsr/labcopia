package com.tecnocampus.tecnomeet.application;

import com.tecnocampus.tecnomeet.application.dto.MatchDTO;
import com.tecnocampus.tecnomeet.domain.Match;
import com.tecnocampus.tecnomeet.domain.Match.Status;
import com.tecnocampus.tecnomeet.persistence.MatchRepository;
import org.springframework.stereotype.Service;

@Service
public class MatchService {
    private final MatchRepository repository;

    public MatchService(MatchRepository repository) {
        this.repository = repository;
    }

    public MatchDTO swipe(String likerId, String likedId) {
        Match pending = repository.findPendingMatch(likerId, likedId);
        if (pending != null) {
            pending.setStatus(Status.MATCHED);
            repository.updateMatch(pending);
            return toDTO(pending);
        }
        Match m = new Match(likerId, likedId);
        repository.addMatch(m);
        return toDTO(m);
    }

    public MatchDTO unmatch(String id) {
        Match m = repository.getMatchById(id);
        m.setStatus(Status.UNMATCHED);
        repository.updateMatch(m);
        return toDTO(m);
    }

    private MatchDTO toDTO(Match m) {
        return new MatchDTO(m.getId(), m.getLikerId(), m.getLikedId(), m.getStatus().name());
    }
}
