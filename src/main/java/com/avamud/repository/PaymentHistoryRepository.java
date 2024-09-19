package com.avamud.repository;

import com.avamud.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentHistoryRepository  extends JpaRepository<PaymentHistory, Long> {
    List<PaymentHistory> findByPaymentId(Long paymentId);
}
