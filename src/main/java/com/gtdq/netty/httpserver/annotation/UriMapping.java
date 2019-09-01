package com.gtdq.netty.httpserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : LiuMing
 * @date : 2019/8/31 16:03
 * @description :   资源映射注解(类似mvc的mapping注解)(@Inherited标记的注解可以被继承)
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UriMapping {
    String value() default "/index";
}
