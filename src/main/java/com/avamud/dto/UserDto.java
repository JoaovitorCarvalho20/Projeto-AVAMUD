package com.avamud.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String nome;
    private String cpf;
    private String cnpj;
    private String email;
    private String senha;
    private String login;
    private String telefone;
    private Date dataDeEntrada;
    private List<AddressDto> addresses; // Lista de endereços associados
    private List<PaymentDto> payments;

    public UserDto() {
    }

}
