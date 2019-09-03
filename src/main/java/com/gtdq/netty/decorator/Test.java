package com.gtdq.netty.decorator;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

/**
 * @author : LiuMingyao
 * @date : 2019/9/3 20:21
 * @description : IO中的装饰器设计模式
 * @see DataInputStream 过滤流的具体装饰器类 extends  {@link FilterInputStream}.
 * @see BufferedInputStream 过滤流的具体装饰器类 extends  {@link FilterInputStream}.
 * @see FilterInputStream  过滤流的顶层类。而该过滤流实现了输入流的顶层接口implements {@link InputStream}.并持有一个对象，通过构造注入
 * @see FileInputStream 具体的输入文件流，实现了InputStream接口
 */
public class Test {
    public static void main(String[] args) throws Exception {
        InputStream inputStream = new DecorateC(new DecorateB(new InputStreamImpl()));
        inputStream.doSomething();
        System.out.println("============类似于下面这种方法===============");
        java.io.InputStream inputStream1 = new DataInputStream(new BufferedInputStream(new FileInputStream("e:/aaa.jpg")));
        inputStream1.read();
    }
}