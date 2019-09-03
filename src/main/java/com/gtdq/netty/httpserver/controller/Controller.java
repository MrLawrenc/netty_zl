package com.gtdq.netty.httpserver.controller;

import com.gtdq.netty.httpserver.annotation.MarsController;
import com.gtdq.netty.httpserver.annotation.UriMapping;

/**
 * @author : LiuMingyao
 * @date : 2019/8/31 16:45
 * @description : TODO
 */
@MarsController
@UriMapping("/mars")
public class Controller {

    @UriMapping("/test")
    public String test() {
        System.out.println(Thread.currentThread().getName() + "调用了controller..........");
        return "success";
    }

    public void test2() {

    }

    @UriMapping("/aaa")
    public void test3(String id) {
        System.out.println("调用controller的test3,id=" + id);
    }
}