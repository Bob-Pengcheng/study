package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author baopc@tuya.com
 * @date 2020/6/24
 */
@RestController
public class HelloWorldController {

    @GetMapping("/hello")
    public String sayHello(String name){
        return "Hello," + name;
    }
}
