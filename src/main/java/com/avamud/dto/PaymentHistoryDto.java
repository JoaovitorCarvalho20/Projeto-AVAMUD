package com.avamud.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentHistoryDto {
    private Long id;
    private String action;
    private LocalDateTime actionDate;
    private Long userId;
    private Long paymentId;
}
