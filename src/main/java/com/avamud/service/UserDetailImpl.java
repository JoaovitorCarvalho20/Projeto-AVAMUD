package com.avamud.service;

import com.avamud.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailImpl implements UserDetails {

    // Atributos que representam os dados do usuário
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;

    // Método estático para construir uma instância de UserDetailImpl a partir de um objeto User
    public static UserDetailImpl build(User user) {
        return new UserDetailImpl(
                user.getId(),        // ID do usuário
                user.getNome(),      // Nome do usuário
                user.getLogin(),     // Nome de login (username)
                user.getEmail(),     // Email do usuário
                user.getSenha(),     // Senha do usuário
                new ArrayList<>()    // Inicialização de authorities vazia (a lista de permissões)
        );
    }

    // Lista de permissões (roles) do usuário
    private Collection<? extends GrantedAuthority> authorities;

    // Métodos exigidos pela interface UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // Retorna a lista de permissões
    }

    @Override
    public String getPassword() {
        return password; // Retorna a senha do usuário
    }

    @Override
    public String getUsername() {
        return username; // Retorna o nome de login (username)
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Indica que a conta não está expirada
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Indica que a conta não está bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Indica que as credenciais (senha) não estão expiradas
    }

    @Override
    public boolean isEnabled() {
        return true; // Indica que a conta está ativa
    }
}
