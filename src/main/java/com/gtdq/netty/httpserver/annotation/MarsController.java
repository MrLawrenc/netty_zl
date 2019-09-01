package com.gtdq.netty.httpserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : LiuMing
 * @date : 2019/8/31 17:11
 * @description :   标注为一个接收http请求的控制器类
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MarsController {

}
