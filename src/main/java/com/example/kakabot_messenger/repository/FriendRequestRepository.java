package com.example.kakabot_messenger.repository;

import com.example.kakabot_messenger.model.FriendRequest;
import com.example.kakabot_messenger.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository
        extends JpaRepository<FriendRequest, Long> {



    Optional<FriendRequest> findBySenderAndReceiver(
            String sender,
            String receiver
    );

    List<FriendRequest> findByReceiverAndStatus(
            String receiver,
            FriendRequest.Status status
    );
}