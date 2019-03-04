package com.zhuxh.hystrix.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhuxh on 17/7/16.
 */
@RequestMapping("hystrix")
@RestController
@DefaultProperties(defaultFallback = "classDefaultFallback")
public class IndexController {

    @GetMapping("hello")
    public String hello() {
        return "Hello world";
    }

    @GetMapping("hello/success")
    @HystrixCommand
    public String helloSuccess() {
        return "Hystrix Hello World!";
    }

    @GetMapping("hello/groupKey")
    @HystrixCommand(groupKey = "MyGroupKey")
    public String helloGroupKey() {
        return "Hystrix Hello World, GroupKey is MyGroupKey";
    }

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100")
    })
    @GetMapping("hello/timeout")
    public String helloTimeout() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(110);
        return "Hello Timeout";
    }

    @GetMapping("exception/test")
    @HystrixCommand(fallbackMethod = "fallback")
    public String expcetionTest() throws NoSuchMethodException {
        int a = 10;
        if (a < 20) {
            throw new NoSuchMethodException();
        }
        return getString();
    }

    private String getString() {
        throw new RuntimeException();
    }


    String fallback() {
        throw new NullPointerException();
//        return "fallback";
    }

    String fallback(String str) {
        return "fallback String ()";
    }

    String defaultFallback() {
        return "defaultFallback";
    }

    String classDefaultFallback() {
        return "classDefaultFallback";
    }
}
