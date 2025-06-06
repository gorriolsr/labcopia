package com.tecnocampus.tecnomeet.api;

import com.tecnocampus.tecnomeet.application.ChatService;
import com.tecnocampus.tecnomeet.application.dto.ChatMessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
public class ChatRestController {
    private final ChatService service;

    public ChatRestController(ChatService service) {
        this.service = service;
    }

    @PostMapping("/{matchId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatMessageDTO send(@PathVariable String matchId, @RequestParam String senderId, @RequestParam String content) {
        return service.sendMessage(matchId, senderId, content);
    }

    @GetMapping("/{matchId}")
    public List<ChatMessageDTO> list(@PathVariable String matchId) {
        return service.getMessages(matchId);
    }
}
