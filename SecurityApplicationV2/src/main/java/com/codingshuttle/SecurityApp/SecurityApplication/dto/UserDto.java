package com.codingshuttle.SecurityApp.SecurityApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private Long id;

    private String email;
    private String name;
   // private int SESSION_LIMIT;
}
