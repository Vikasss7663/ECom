package com.ecom.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    private String cartId;
    @NotBlank(message = "cart.userId must be present")
    private String userId;
    private LocalDate createdAt;
    private LocalDate modifiedAt;
}
