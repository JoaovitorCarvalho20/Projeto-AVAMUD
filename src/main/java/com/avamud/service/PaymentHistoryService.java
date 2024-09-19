package com.avamud.service;

import com.avamud.dto.PaymentHistoryDto;
import com.avamud.entity.Payment;
import com.avamud.entity.PaymentHistory;
import com.avamud.entity.User; // Importação da entidade User
import com.avamud.repository.PaymentHistoryRepository;
import com.avamud.repository.UserRepository; // Importar o repositório de User
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentHistoryService {

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    private UserRepository userRepository; // Injeção do repositório User

    // Método para buscar o histórico de um pagamento por ID
    public List<PaymentHistoryDto> getPaymentHistoryByPaymentId(Long paymentId) {
        List<PaymentHistory> historyList = paymentHistoryRepository.findByPaymentId(paymentId);
        return historyList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    // Método para buscar todo hidtorico
    public List<PaymentHistoryDto> getAllPaymentHistory() {
        List<PaymentHistory> historyList = paymentHistoryRepository.findAll();
        return historyList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    // Converter entidade para DTO
    private PaymentHistoryDto convertToDto(PaymentHistory history) {
        PaymentHistoryDto dto = new PaymentHistoryDto();
        dto.setId(history.getId());
        dto.setAction(history.getAction());
        dto.setActionDate(history.getActionDate());

        // Acesso ao usuário através da entidade User
        dto.setUserId(history.getUser() != null ? history.getUser().getId() : null);

        dto.setPaymentId(history.getPayment().getId());
       // dto.setUserId(history.getUser().getId()); acredito que o erro de nao mostrar userID venha daqui
        return dto;
    }

    // Método para salvar o histórico
    public void savePaymentHistory(Payment payment, String action, Long userId) {
        PaymentHistory history = new PaymentHistory();
        history.setAction(action);
        history.setActionDate(LocalDateTime.now());
        history.setPayment(payment); // Define o pagamento

        // Cria um objeto User e atribui ao histórico
        User user = new User();
        user.setId(userId);
        history.setPayment(payment); // Mantém a referência ao pagamento
        history.setUser(user); // Define o usuário no histórico

        paymentHistoryRepository.save(history);
    }



}
