package com.avamud.service;

import com.avamud.dto.AddressDto;
import com.avamud.dto.UserDto;
import com.avamud.entity.Address;
import com.avamud.entity.User;
import com.avamud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository; // Repositório para operações com o banco de dados

    @Autowired
    private PasswordEncoder passwordEncoder; // Encoder para codificar senhas

    // Converte uma entidade User para um DTO UserDto
    private UserDto converterDTO(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setNome(user.getNome());
        dto.setCpf(user.getCpf());
        dto.setCnpj(user.getCnpj());
        dto.setTelefone(user.getTelefone());
        dto.setEmail(user.getEmail());
        dto.setSenha(user.getSenha());
        dto.setLogin(user.getLogin());
        dto.setDataDeEntrada(user.getDataDeEntrada());

        // Converte a lista de endereços, verificando se não é null
        List<AddressDto> addressDtos = (user.getAddresses() != null ?
                user.getAddresses().stream()
                        .map(this::converterAddressDTO)
                        .collect(Collectors.toList()) : new ArrayList<>());
        dto.setAddresses(addressDtos);

        return dto;
    }

    // Converte uma entidade Address para um DTO AddressDto
    private AddressDto converterAddressDTO(Address address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setRua(address.getRua());
        dto.setNumero(address.getNumero());
        dto.setBairro(address.getBairro());
        dto.setCidade(address.getCidade());
        dto.setEstado(address.getEstado());
        dto.setCep(address.getCep());
        dto.setUserId(address.getUser().getId()); // Associa o endereço ao ID do usuário
        return dto;
    }

    // Converte um DTO AddressDto para uma entidade Address
    private Address converterAddressEntity(AddressDto dto) {
        Address address = new Address();
        address.setId(dto.getId());
        address.setRua(dto.getRua());
        address.setNumero(dto.getNumero());
        address.setBairro(dto.getBairro());
        address.setCidade(dto.getCidade());
        address.setEstado(dto.getEstado());
        address.setCep(dto.getCep());
        // A relação inversa (User) deve ser configurada ao criar o usuário
        return address;
    }

    // Converte um DTO UserDto para uma entidade User
    private User converterEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setNome(dto.getNome());
        user.setCpf(dto.getCpf());
        user.setCnpj(dto.getCnpj());
        user.setEmail(dto.getEmail());
        user.setSenha(dto.getSenha());
        user.setLogin(dto.getLogin());
        user.setTelefone(dto.getTelefone());
        user.setDataDeEntrada(dto.getDataDeEntrada());

        // Converte endereços do DTO para entidades e associa ao usuário
        List<Address> addresses = dto.getAddresses().stream()
                .map(this::converterAddressEntity)
                .collect(Collectors.toList());

        user.setAddresses(addresses);
        // Configura a relação inversa para cada endereço
        addresses.forEach(address -> address.setUser(user));

        return user;
    }

    // Retorna todos os usuários, convertendo de entidade para DTO
    public List<UserDto> getAllUser() {
        return userRepository.findAll().stream()
                .map(this::converterDTO)
                .collect(Collectors.toList());
    }

    // Retorna um usuário específico pelo ID, convertendo de entidade para DTO
    public UserDto getUserPeloId(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(this::converterDTO).orElse(null);
    }

    // Deleta um usuário pelo ID e retorna verdadeiro se a operação for bem-sucedida
    public Boolean deletarUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    // Atualiza um usuário existente com os dados do DTO e retorna o usuário atualizado como DTO
    public UserDto atualizarUser(Long id, UserDto userDto) {
        if (userRepository.existsById(id)) {
            User user = converterEntity(userDto);
            user.setId(id); // Garante que o ID do usuário não seja alterado
            user.setSenha(passwordEncoder.encode(user.getSenha())); // Codifica a senha antes de salvar
            User atualizarUser = userRepository.save(user);
            return converterDTO(atualizarUser);
        } else {
            return null;
        }
    }

    // Cria um novo usuário com os dados do DTO, codifica a senha e retorna o usuário criado como DTO
    public UserDto criarUser(UserDto userDto) {
        // Verifica se o campo login está presente e não é vazio
        if (userDto.getLogin() == null || userDto.getLogin().isEmpty()) {
            throw new IllegalArgumentException("O campo login é obrigatório");
        }

        User user = converterEntity(userDto);
        user.setSenha(passwordEncoder.encode(user.getSenha())); // Codifica a senha antes de salvar
        User salvarUser = userRepository.save(user);
        return converterDTO(salvarUser);
    }
}
