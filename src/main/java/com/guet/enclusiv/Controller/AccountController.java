package com.guet.enclusiv.Controller;


import com.guet.enclusiv.DTO.LoginForm;
import com.guet.enclusiv.DTO.RegisterForm;
import com.guet.enclusiv.DTO.RequestResult;
import com.guet.enclusiv.Entity.User;
import com.guet.enclusiv.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/account")
public class AccountController {

    private final UserService userService;

    @Autowired
    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("user")
    public RequestResult<User> getUser(@RequestParam("id") long userId){
        return userService.getUser(userId);
    }

    @PostMapping("/login")
    public RequestResult<User> login(@RequestBody LoginForm loginForm){

        return userService.login(loginForm);

    }

    @PostMapping("/register")
    public RequestResult<Long> register(@RequestBody RegisterForm registerForm){

        return userService.register(registerForm);

    }

    @PostMapping("/change")
    public RequestResult<Boolean> changePasswd(@RequestBody LoginForm changeForm){

        return userService.changePasswd(changeForm);
    }

}
