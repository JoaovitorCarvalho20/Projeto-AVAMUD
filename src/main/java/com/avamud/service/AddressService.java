package com.avamud.service;

import com.avamud.dto.AddressDto;
import com.avamud.entity.Address;
import com.avamud.entity.User;
import com.avamud.repository.AddressRepository;
import com.avamud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    private AddressDto converterDTO(Address address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setRua(address.getRua());
        dto.setNumero(address.getNumero());
        dto.setBairro(address.getBairro());
        dto.setCidade(address.getCidade());
        dto.setEstado(address.getEstado());
        dto.setCep(address.getCep());
        dto.setUserId(address.getUser().getId());
        return dto;
    }

    private Address converterEntity(AddressDto dto) {
        Address address = new Address();
        address.setId(dto.getId());
        address.setRua(dto.getRua());
        address.setNumero(dto.getNumero());
        address.setBairro(dto.getBairro());
        address.setCidade(dto.getCidade());
        address.setEstado(dto.getEstado());
        address.setCep(dto.getCep());

        Optional<User> user = userRepository.findById(dto.getUserId());
        user.ifPresent(address::setUser);

        return address;
    }

    public List<AddressDto> getAllAddress() {
        return addressRepository.findAll().stream()
                .map(this::converterDTO)
                .collect(Collectors.toList());
    }

    public AddressDto getAddressPeloId(Long id) {
        Optional<Address> address = addressRepository.findById(id);
        return address.map(this::converterDTO).orElse(null);
    }

    public Boolean deletarAddress(Long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public AddressDto atualizarAddress(Long id, AddressDto addressDto) {
        if (addressRepository.existsById(id)) {
            Address address = converterEntity(addressDto);
            address.setId(id);
            Address atualizarAddress = addressRepository.save(address);
            return converterDTO(atualizarAddress);
        } else {
            return null;
        }
    }

    public AddressDto criarAddress(AddressDto addressDto) {
        Address address = converterEntity(addressDto);
        Address salvarAddress = addressRepository.save(address);
        return converterDTO(salvarAddress);
    }
}
