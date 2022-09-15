package com.guet.enclusiv.Service;

import com.guet.enclusiv.DTO.RequestResult;

public interface CheckCodeService {

    RequestResult<String> generateCode(String email);

    RequestResult<Boolean> checkUserCode(String email, String code);

}
