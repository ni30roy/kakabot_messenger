package com.example.kakabot_messenger.controller;

import com.example.kakabot_messenger.dto.MessageRequest;
import com.example.kakabot_messenger.model.Message;
import com.example.kakabot_messenger.repository.MessageRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageRepository repo;
    private static final String ADMIN = "ramukaka";

    public MessageController(MessageRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/send")
    public void send(@RequestBody MessageRequest req,
                     Authentication auth) {

        Message m = new Message();
        m.setSender(auth.getName());
        m.setReceiver(req.getReceiver());
        m.setContent(req.getContent());
        m.setCreatedAt(LocalDateTime.now());

        repo.save(m);
    }

    @GetMapping("/chat/{otherUser}")
    public List<Message> chat(@PathVariable String otherUser,
                              Authentication auth) {

        return repo.findChatBetween(
                auth.getName(),
                otherUser
        );
    }

    @GetMapping("/all")
    public List<Message> all(Authentication auth) {
        if (!auth.getName().equals(ADMIN)) {
            throw new RuntimeException("Unauthorized");
        }
        return repo.findAllByOrderByCreatedAtAsc();
    }
}