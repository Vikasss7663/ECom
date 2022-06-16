package com.ecom.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private String categoryId;
    private String categoryName;
    private String categoryDesc;
    private Date createdAt;
    private Date modifiedAt;
}