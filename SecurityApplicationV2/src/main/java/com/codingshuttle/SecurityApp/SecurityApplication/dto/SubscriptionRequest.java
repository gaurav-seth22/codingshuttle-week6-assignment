package com.codingshuttle.SecurityApp.SecurityApplication.dto;

import lombok.Data;

@Data
public class SubscriptionRequest {

    private Long userId;
    private String planName;
}
