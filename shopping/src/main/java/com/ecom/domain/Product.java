package com.ecom.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private String productId;
    private String productName;
    private String productDesc;
    private Double productPrice;
    private String productImageUrl;
    private long productQuantity;
    private String categoryId;
    private LocalDate createdAt;
    private LocalDate modifiedAt;
}