package com.guet.enclusiv.DTO;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegisterForm {

    private String email;

    private String name;

    private String password;

}
