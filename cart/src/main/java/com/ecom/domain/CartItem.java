package com.ecom.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class CartItem {

    @Id
    private String cartItemId;
    @NotBlank(message = "cart.cartId must be present")
    private String cartId;
    @NotBlank(message = "cart.productId must be present")
    private String productId;
    @Min(value = 1, message = "cart.productQuantity must be more than 0")
    private int quantity;
    private LocalDate createdAt = LocalDate.now();
    private LocalDate modifiedAt = LocalDate.now();
}
