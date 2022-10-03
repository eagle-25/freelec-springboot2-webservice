package com.eagle25.practice.springboot.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController는 컨트롤러를 JSON을 반환하는 컨트롤러를 만들어준다.
@RestController
public class HelloController {

    @GetMapping("/hello") // HTTP Get 메쏘드의 요청을 받을 수 있는 API를 만들어준다.
    public String hello() {
        return "hello";
    }
}
