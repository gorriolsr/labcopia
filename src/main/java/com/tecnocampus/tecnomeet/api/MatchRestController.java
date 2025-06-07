package com.tecnocampus.tecnomeet.api;

import com.tecnocampus.tecnomeet.application.MatchService;
import com.tecnocampus.tecnomeet.application.dto.MatchDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matches")
public class MatchRestController {
    private final MatchService service;

    public MatchRestController(MatchService service) {
        this.service = service;
    }

    @PostMapping("/swipe/{likerId}/{likedId}")
    @ResponseStatus(HttpStatus.CREATED)
    public MatchDTO swipe(@PathVariable String likerId, @PathVariable String likedId) {
        return service.swipe(likerId, likedId);
    }

    @DeleteMapping("/{id}")
    public MatchDTO unmatch(@PathVariable String id) {
        return service.unmatch(id);
    }
}
