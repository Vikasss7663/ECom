package com.ecom.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Order {

    @Id
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