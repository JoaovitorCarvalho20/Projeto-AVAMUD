package com.avamud.repository;

import com.avamud.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByLogin(String login); // Certifique-se que é Optional<User>
  Optional<User> findByNome(String nome);
}
