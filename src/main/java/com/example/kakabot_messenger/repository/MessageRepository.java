package com.example.kakabot_messenger.repository;

import com.example.kakabot_messenger.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // ✅ CORRECT & SAFE QUERY
    @Query("""
        SELECT m FROM Message m
        WHERE (m.sender = :u1 AND m.receiver = :u2)
           OR (m.sender = :u2 AND m.receiver = :u1)
        ORDER BY m.createdAt ASC
    """)
    List<Message> findChatBetween(
            @Param("u1") String user1,
            @Param("u2") String user2
    );

    // Admin helper
    List<Message> findAllByOrderByCreatedAtAsc();
}