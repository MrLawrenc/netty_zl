package com.gtdq.netty.httpserver.controller;

import com.gtdq.netty.httpserver.annotation.MarsController;
import com.gtdq.netty.httpserver.annotation.UriMapping;

/**
 * @author : LiuMingyao
 * @date : 2019/9/3 14:13
 * @description : TODO
 */
@MarsController
public class TestController {

    @UriMapping("/tt")
    public void test() {
        System.out.println("tt...............");
    }
}