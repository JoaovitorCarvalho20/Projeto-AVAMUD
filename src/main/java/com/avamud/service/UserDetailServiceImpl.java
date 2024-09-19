package com.avamud.service;

import com.avamud.entity.User;
import com.avamud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // Certifica-se de que a classe é registrada como um bean Spring
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Procura o usuário no repositório pelo nome de login
        User user = userRepository.findByLogin(username);

        // Verifica se o usuário foi encontrado
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Constrói e retorna UserDetailImpl com base no usuário encontrado
        return UserDetailImpl.build(user);
    }
}
