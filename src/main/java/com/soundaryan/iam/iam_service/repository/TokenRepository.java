package com.soundaryan.iam.iam_service.repository;

import com.soundaryan.iam.iam_service.model.Token;
import com.soundaryan.iam.iam_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
    void deleteAllByUser(User user); // useful when revoking tokens
}
