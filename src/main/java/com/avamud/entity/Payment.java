package com.avamud.entity;


import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private BigDecimal valor;
    @NotNull
    private LocalDateTime dataPagamento;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
