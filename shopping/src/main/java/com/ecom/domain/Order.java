package com.ecom.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private String orderId;
    @NotBlank(message = "cart.userId must be present")
    private String userId;
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    private LocalDate createdAt;
    private LocalDate modifiedAt;
}

enum PaymentStatus {

    PENDING,
    COMPLETED,
    FAILED
}