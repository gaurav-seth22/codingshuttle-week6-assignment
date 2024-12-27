package com.codingshuttle.SecurityApp.SecurityApplication.dto;


import com.codingshuttle.SecurityApp.SecurityApplication.entities.enums.Permission;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.enums.Role;

import lombok.Data;

import java.util.Set;


@Data
public class SignupDto {

    private String email;
    private String password;
    private String name;
    private Set<Role> roles;
    private Set<Permission> permissions;


}
