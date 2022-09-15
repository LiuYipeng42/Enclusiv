package com.guet.enclusiv.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guet.enclusiv.DTO.LoginForm;
import com.guet.enclusiv.DTO.RegisterForm;
import com.guet.enclusiv.DTO.RequestResult;
import com.guet.enclusiv.Entity.User;

import java.util.List;

public interface UserService extends IService<User> {

    RequestResult<User> login(LoginForm loginForm);

    RequestResult<User> getUser(long userId);

    RequestResult<Long> register(RegisterForm registerForm);

    RequestResult<Boolean> changePasswd(LoginForm registerForm);

    RequestResult<Boolean> view(long userId, long articleId);

    RequestResult<Boolean> like(long userId, long articleId);

    RequestResult<Boolean> dislike(long userId, long articleId);

    RequestResult<List<Long>> likes(long userId);

    RequestResult<Boolean> collect(long userId, long articleId);

    RequestResult<Boolean> cancelCollect(long userId, long articleId);

    RequestResult<List<Long>> collections(long userId);

    RequestResult<Boolean> subscribe(long userId, long authorId);

    RequestResult<Boolean> cancelSubscribe(long userId, long authorId);

    RequestResult<List<Long>> subscribes(long userId);

    RequestResult<List<Long>> followers(long authorId);
}
