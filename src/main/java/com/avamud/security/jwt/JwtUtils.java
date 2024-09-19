package com.avamud.security.jwt;

import com.avamud.service.UserDetailImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {

    // Injeta o valor do secret do JWT a partir do arquivo de configuração (application.properties ou application.yml)
    @Value("${projeto.jwtSecret}")
    private String jwtSecret;

    // Injeta o valor da expiração do JWT em milissegundos a partir do arquivo de configuração
    @Value("${projeto.jwtExpiration}")
    private int jwtExpiration;

    // Método para gerar um token JWT com base nos detalhes do usuário
    public String generateTokenFromUserDetailsImpl(UserDetailImpl userDetail) {
        return Jwts.builder()
                // Define o nome de usuário como o "subject" do token JWT
                .setSubject(userDetail.getUsername())
                // Define a data de criação do token
                .setIssuedAt(new Date())
                // Define a data de expiração do token com base na expiração configurada
                .setExpiration(new Date(new Date().getTime() + jwtExpiration))
                // Define o algoritmo de assinatura e a chave secreta do JWT
                .signWith(getSigninKey(), SignatureAlgorithm.HS512)
                // Compacta o token e o retorna como uma string
                .compact();
    }

    // Método que gera a chave de assinatura a partir da chave secreta (jwtSecret) configurada
    public Key getSigninKey() {
        // Decodifica a chave secreta de Base64 para bytes
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        // Usa a chave decodificada para criar uma chave HMAC SHA
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        return key; // Retorna a chave usada para assinar o token JWT
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(getSigninKey()).build().parseClaimsJws(token).getBody().getSubject();}


    public Boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(getSigninKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            System.out.println("token invalido");
        }catch (ExpiredJwtException e) {
            System.out.println("token expirado");
        }catch (UnsupportedJwtException e) {
            System.out.println("token invalido");
        }catch (SignatureException e) {
            System.out.println("token invalido");
        }catch (IllegalArgumentException e) {
            System.out.println("token invalido");
        }
        return false;
    }
}
