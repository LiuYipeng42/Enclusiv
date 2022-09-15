package com.guet.enclusiv.Controller;


import com.guet.enclusiv.DTO.RequestResult;
import com.guet.enclusiv.Service.CheckCodeService;
import com.guet.enclusiv.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/checkcode")
public class CheckCodeController {

    private final CheckCodeService checkCodeService;

    private final MailService mailService;

    @Autowired
    public CheckCodeController(CheckCodeService checkCodeService, MailService mailService) {
        this.checkCodeService = checkCodeService;
        this.mailService = mailService;
    }

    @GetMapping("/get")
    public RequestResult<String> getCode(@RequestParam("email") String email){

        RequestResult<String> requestResult = checkCodeService.generateCode(email);
        mailService.send(email, String.valueOf(requestResult.getData()));

        return requestResult;
    }

    @GetMapping("check")
    public RequestResult<Boolean> checkCode(@RequestParam("email") String email, @RequestParam("code") String code){

        return checkCodeService.checkUserCode(email, code);
    }

}
