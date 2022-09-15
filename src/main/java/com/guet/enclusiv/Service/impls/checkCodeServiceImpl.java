package com.guet.enclusiv.Service.impls;


import com.guet.enclusiv.DTO.RequestResult;
import com.guet.enclusiv.Service.CheckCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class checkCodeServiceImpl implements CheckCodeService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public checkCodeServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public RequestResult<String> generateCode(String email) {

        String checkCode = String.valueOf((int)(Math.random() * 899999 + 100000));

        redisTemplate.opsForValue().set(
                email,
                checkCode,
                5 * 60,
                TimeUnit.SECONDS
        );

        return new RequestResult<String>().setData(checkCode).setResult("success");
    }

    @Override
    public RequestResult<Boolean> checkUserCode(String email, String code) {

        String checkCode = String.valueOf(redisTemplate.opsForValue().get(email));

        if (!checkCode.equals("null")){
            if (checkCode.equals(code)){
                return new RequestResult<Boolean>().setData(true).setResult("success");
            }else {
                return new RequestResult<Boolean>().setData(false).setResult("code not the same");
            }
        }else {
            return new RequestResult<Boolean>().setData(false).setResult("code not exist");
        }

    }
}
