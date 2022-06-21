package com.ecom.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private String cartItemId;
    @NotBlank(message = "cart.cartId must be present")
    private String cartId;
    @NotBlank(message = "cart.productId must be present")
    private String productId;
    @Min(value = 1, message = "cart.productQuantity must be more than 0")
    private long quantity;
    private LocalDate createdAt = LocalDate.now();
    private LocalDate modifiedAt = LocalDate.now();
}
