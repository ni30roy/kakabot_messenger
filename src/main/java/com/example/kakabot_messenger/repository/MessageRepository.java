package com.example.kakabot_messenger.repository;

import com.example.kakabot_messenger.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderAndReceiverOrSenderAndReceiverOrderByCreatedAtAsc(
            String sender1, String receiver1,
            String sender2, String receiver2
    );

    Optional<Message> findTopBySenderOrReceiverOrderByCreatedAtDesc(
            String sender, String receiver
    );

    List<Message> findAllByOrderByCreatedAtAsc();
}