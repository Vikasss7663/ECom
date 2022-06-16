package com.ecom.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisOrder {

    private String orderId;
    private String userId;
    private Date createdAt;
    private Date modifiedAt;
}
