package com.avamud.repository;

import com.avamud.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


  User findByLogin(String login);
}
