package com.wiliamhw.springbootrestapi.helloworld;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldResource {

    @RequestMapping("/hello-world")
    public String helloWord() {
        return "Hello World";
    }

    @RequestMapping("/hello-world-bean")
    public HelloWorldBean helloWordBean() {
        return new HelloWorldBean("Hello World");
    }

    @RequestMapping("/hello-world-path-param/{name}/message/{message}")
    public HelloWorldBean helloWordBeanMultiplePathParam(@PathVariable String name, @PathVariable String message) {
        return new HelloWorldBean("Hello World " + name + ", " + message);
    }
}
