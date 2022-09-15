package com.guet.enclusiv.DTO;

import com.guet.enclusiv.Entity.User;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class UserInfo {

    private Long id;

    private String name;

    private String email;

    private  String signature;

    public UserInfo(){

    }

    public UserInfo(User user){
        id = user.getId();
        name = user.getName();
        email = user.getEmail();
        signature = user.getSignature();
    }

}
