package com.ps.controller;

import com.ps.domain.Member;
import com.ps.domain.Result;
import com.ps.service.RegisterService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author JIANGZI
 */
@RestController
@RequestMapping("/h5")
public class RegisterController {

    @Reference(version = "1.0.0")
    private RegisterService registerService;

    @RequestMapping("/register")
    public Integer register(@RequestBody Member member){

        Integer register = registerService.register(member);

        return register;

    }

    @PostMapping("/integral/{memberId}")
    public Result integral(@PathVariable("memberId")int memberId){

        Result result = registerService.queryIntegral(memberId);
        return result;
    }




}
