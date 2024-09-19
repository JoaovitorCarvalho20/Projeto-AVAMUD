package com.avamud.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntrypointJwt implements AuthenticationEntryPoint {

    // Este método é chamado quando uma exceção de autenticação ocorre, por exemplo, quando um usuário não autenticado
    // tenta acessar um recurso protegido sem fornecer um token JWT válido.
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // Define o tipo de resposta como JSON, para que o cliente saiba que está recebendo dados no formato JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Define o status da resposta como 401 (Unauthorized) indicando que o usuário não está autenticado
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Cria um corpo de resposta para fornecer mais informações ao cliente sobre o erro
        final Map<String, Object> body = new HashMap<>();

        // Adiciona o código de status HTTP ao corpo da resposta
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);

        // Adiciona uma mensagem de erro simples indicando que o acesso foi negado
        body.put("error", "Unauthorized");

        // Cria um ObjectMapper para converter o mapa em JSON
        final ObjectMapper mapper = new ObjectMapper();

        // Escreve o corpo da resposta (que está no formato de mapa) como JSON no fluxo de saída da resposta HTTP
        mapper.writeValue(response.getOutputStream(), body);
    }
}
