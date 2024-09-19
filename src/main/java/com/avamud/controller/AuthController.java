package com.avamud.controller;

import com.avamud.dto.AcessDto;
import com.avamud.dto.AddressDto;
import com.avamud.dto.AuthDto;
import com.avamud.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<AcessDto> login(@RequestBody AuthDto authDto) {
        AcessDto acessDto = authService.login(authDto);
        return ResponseEntity.ok(acessDto);
    }

}
