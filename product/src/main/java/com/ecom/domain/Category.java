package com.ecom.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Category {

    @Id
    private String categoryId;
    @NotBlank(message = "category.name must be present")
    private String categoryName;
    @NotBlank(message = "category.description must be present")
    private String categoryDesc;
    private Date createdAt;
    private Date modifiedAt;
}