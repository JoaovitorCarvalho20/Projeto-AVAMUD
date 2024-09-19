package com.avamud.service;

import com.avamud.dto.PaymentDto;
import com.avamud.entity.Payment;
import com.avamud.entity.User;
import com.avamud.repository.PaymentRepository;
import com.avamud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private PaymentHistoryService paymentHistoryService;

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
    // Dentro do método onde você precisa pegar o usuário logado

    // Método para obter o ID do usuário logado
    private Long getLoggedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Chame findByLogin e trate como Optional
            Optional<User> user = userRepository.findByLogin(userDetails.getUsername());

            return user.map(User::getId).orElse(null);
        }
        return null;
    }






    // Método para deletar um pagamento
    public Boolean deletePayment(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);

        if (payment.isPresent()) {
            paymentRepository.deleteById(id);

            // Obtenha o ID do usuário logado
            Long loggedUserId = getLoggedUserId();

            // Salvar histórico com o ID do usuário
            paymentHistoryService.savePaymentHistory(payment.get(), "Deletado", loggedUserId);
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
            paymentHistoryService.savePaymentHistory(atualizar,"Atualizado",dto.getId());
            return converterDto(atualizar);
        } else {
            return null;
        }
    }

    public PaymentDto criarPayment(PaymentDto dto) {
        Payment payment = converterEntity(dto);
        Payment salvarPayment = paymentRepository.save(payment);


        Long userId = dto.getUserId(); // Verifique se o ID do usuário está no PaymentDto

        // Salvar histórico
        paymentHistoryService.savePaymentHistory(salvarPayment, "Criado", userId);

        return converterDto(salvarPayment);
    }

}
