package com.example.kakabot_messenger.controller;

import com.example.kakabot_messenger.model.FriendRequest;
import com.example.kakabot_messenger.repository.FriendRequestRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/friend")
public class FriendRequestController {

    private final FriendRequestRepository repo;
    private static final String ADMIN = "ramukaka";

    public FriendRequestController(FriendRequestRepository repo) {
        this.repo = repo;
    }

    // 1️⃣ User → ramukaka friend request
    @PostMapping("/request")
    public String sendRequest(Authentication auth) {

        String sender = auth.getName();

        if (sender.equals(ADMIN)) {
            return "Admin cannot send request";
        }

        if (repo.findBySenderAndReceiver(sender, ADMIN).isPresent()) {
            return "Request already sent";
        }

        FriendRequest fr = new FriendRequest();
        fr.setSender(sender);
        fr.setReceiver(ADMIN);
        fr.setStatus(FriendRequest.Status.PENDING);

        repo.save(fr);
        return "Friend request sent to ramukaka";
    }

    // 2️⃣ ramukaka sees pending requests
    @GetMapping("/pending")
    public List<FriendRequest> pending(Authentication auth) {

        if (!auth.getName().equals("ramukaka")) {
            throw new RuntimeException("Unauthorized");
        }

        return repo.findByReceiverAndStatus(
                "ramukaka",
                FriendRequest.Status.PENDING
        );
    }

    // 3️⃣ ramukaka accepts request
    @PostMapping("/accept/{id}")
    public String accept(@PathVariable Long id,
                         Authentication auth) {

        if (!auth.getName().equals(ADMIN)) {
            throw new RuntimeException("Unauthorized");
        }

        FriendRequest fr = repo.findById(id).orElseThrow();
        fr.setStatus(FriendRequest.Status.ACCEPTED);
        repo.save(fr);

        return "Accepted";
    }

    // 4️⃣ ⭐ STATUS CHECK (YAHI WALA METHOD)
    @GetMapping("/status")
    public String status(Authentication auth) {

        String user = auth.getName();

        if (user.equals(ADMIN)) {
            return "ADMIN";
        }

        return repo.findBySenderAndReceiver(user, ADMIN)
                .map(fr -> fr.getStatus().name())
                .orElse("NONE");
    }
}