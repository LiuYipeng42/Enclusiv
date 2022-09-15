package com.guet.enclusiv.DTO;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginForm {
    private String email;
    private String password;
}
