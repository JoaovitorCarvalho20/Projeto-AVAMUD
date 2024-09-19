package com.avamud.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentHistory {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private LocalDateTime actionDate;

    @ManyToOne
    @JoinColumn(name = "user_id") // Mapeamento para a entidade User
    private User user;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
