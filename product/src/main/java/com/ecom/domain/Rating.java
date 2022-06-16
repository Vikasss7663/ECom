package com.ecom.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Rating {

    @Id
    private String ratingId;
    @NotBlank(message = "rating.userId must be present")
    private String userId;
    @NotBlank(message = "rating.productId must be present")
    private String productId;
    @NotNull
    @Positive(message = "rating.rating must be positive")
    private Double rating;
    @NotBlank(message = "rating.comment must be present")
    private String comment;
    private Date createdAt;
    private Date modifiedAt;
}