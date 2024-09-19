package com.avamud.service;

import com.avamud.dto.PaymentDto;
import com.avamud.entity.Payment;
import com.avamud.entity.User;
import com.avamud.repository.PaymentRepository;
import com.avamud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    // Método para converter de entidade Payment para DTO PaymentDto
    private PaymentDto converterDto(Payment payment) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setId(payment.getId());
        paymentDto.setValor(payment.getValor());
        paymentDto.setDataPagamento(payment.getDataPagamento());

        // Verifique se o usuário não é nulo antes de acessar seu ID
        if (payment.getUser() != null) {
            paymentDto.setUserId(payment.getUser().getId());
        } else {
            paymentDto.setUserId(null);
        }

        return paymentDto;
    }
    private Payment converterEntity(PaymentDto dto) {
        Payment payment = new Payment();
        payment.setId(dto.getId());
        payment.setValor(dto.getValor());
        payment.setDataPagamento(dto.getDataPagamento());

        // Verifique se userId não é nulo antes de buscar o User
        if (dto.getUserId() != null) {
            Optional<User> user = userRepository.findById(dto.getUserId());
            user.ifPresent(payment::setUser);
        }

        return payment;
    }




    // Método para listar todos os pagamentos
    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::converterDto)
                .collect(Collectors.toList());
    }

    // Método para buscar um pagamento pelo ID
    public PaymentDto getById(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        return payment.map(this::converterDto).orElse(null);
    }

    // Método para deletar um pagamento pelo ID
    public Boolean deletePayment(Long id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    // Método para atualizar um pagamento
    public PaymentDto atualizarPayment(Long id, PaymentDto dto) {
        if (paymentRepository.existsById(id)) {
            Payment payment = converterEntity(dto);
            payment.setId(id); // Garantir que o ID é mantido para a atualização
            Payment atualizar = paymentRepository.save(payment);
            return converterDto(atualizar);
        } else {
            return null;
        }
    }

    // Método para criar um novo pagamento
    public PaymentDto criarPayment(PaymentDto dto) {
        Payment payment = converterEntity(dto);
        Payment salvarPayment = paymentRepository.save(payment);
        return converterDto(salvarPayment);
    }
}
