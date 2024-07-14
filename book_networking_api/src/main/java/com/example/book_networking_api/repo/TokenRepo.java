package com.example.book_networking_api.repo;

import com.example.book_networking_api.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
@EnableJpaRepositories
public interface TokenRepo extends JpaRepository<Token, Integer> {

    Optional<Token> findByToken(String token);
}
