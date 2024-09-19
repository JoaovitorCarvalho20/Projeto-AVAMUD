package com.avamud.controller;

import com.avamud.dto.AddressDto;
import com.avamud.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping
    public List<AddressDto> getAllAddress() {
        return addressService.getAllAddress();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddressPeloId(@PathVariable Long id) {
        AddressDto addressDto = addressService.getAddressPeloId(id);
        if (addressDto != null) {
            return ResponseEntity.ok(addressDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<AddressDto> criarAddress(@RequestBody AddressDto addressDto) {
        AddressDto criarAddress = addressService.criarAddress(addressDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criarAddress);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> atualizarAddress(@PathVariable Long id, @RequestBody AddressDto addressDto) {
        AddressDto atualizarAddress = addressService.atualizarAddress(id, addressDto);
        if (atualizarAddress != null) {
            return ResponseEntity.ok(atualizarAddress);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagarAddress(@PathVariable Long id) {
        if (addressService.deletarAddress(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
