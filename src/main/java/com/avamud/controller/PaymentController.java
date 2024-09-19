package com.avamud.controller;

import com.avamud.dto.PaymentDto;
import com.avamud.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Endpoint para obter todos os pagamentos
    @GetMapping
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        List<PaymentDto> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    // Endpoint para obter um pagamento pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Long id) {
        PaymentDto payment = paymentService.getById(id);
        if (payment != null) {
            return ResponseEntity.ok(payment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para criar um novo pagamento
    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto paymentDto) {
        PaymentDto novoPayment = paymentService.criarPayment(paymentDto);
        return ResponseEntity.ok(novoPayment);
    }

    // Endpoint para atualizar um pagamento existente
    @PutMapping("/{id}")
    public ResponseEntity<PaymentDto> updatePayment(@PathVariable Long id, @RequestBody PaymentDto paymentDto) {
        PaymentDto atualizadoPayment = paymentService.atualizarPayment(id, paymentDto);
        if (atualizadoPayment != null) {
            return ResponseEntity.ok(atualizadoPayment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para deletar um pagamento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        Boolean deletado = paymentService.deletePayment(id);
        if (deletado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
