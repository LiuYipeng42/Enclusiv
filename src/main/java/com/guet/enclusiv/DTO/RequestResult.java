package com.guet.enclusiv.DTO;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RequestResult<T> {

    private T data;

    private String result;

}
