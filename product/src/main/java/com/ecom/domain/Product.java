package com.ecom.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Product {

    @Id
    private String productId;
    @NotBlank(message = "product.name must be present")
    private String productName;
    @NotNull
    @Positive(message = "product.price must be positive")
    private Double productPrice;
    @NotBlank(message = "product.category must be present")
    private String categoryId;
    private Date createdAt;
    private Date modifiedAt;
}