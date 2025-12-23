package com.example.kakabot_messenger.controller;

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

    // ✅ SEND MESSAGE
    @PostMapping("/send")
    public void send(@RequestBody Message req, Authentication auth) {

        String sender = auth.getName();
        String receiver;

        if (sender.equals(ADMIN)) {
            if (req.getReceiver() == null || req.getReceiver().isBlank()) {
                throw new RuntimeException("Admin must select a user");
            }
            receiver = req.getReceiver();
        } else {
            receiver = ADMIN;
        }

        Message m = new Message();
        m.setSender(sender);
        m.setReceiver(receiver);
        m.setContent(req.getContent());
        m.setCreatedAt(LocalDateTime.now());

        repo.save(m);
    }

    // ✅ LOAD CHAT
    @GetMapping("/chat")
    public List<Message> chat(
            @RequestParam String with,
            Authentication auth) {

        String me = auth.getName();

        return repo.findBySenderAndReceiverOrSenderAndReceiverOrderByCreatedAtAsc(
                me, with,
                with, me
        );
    }
}