package com.avamud.service;

import com.avamud.entity.User;
import com.avamud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByLogin(username);

        // Descompacte o Optional
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Retorne sua implementação personalizada
        return UserDetailImpl.build(user); // Usando o método estático que você criou
    }

}
