package com.gtdq.netty.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

/**
 * @author : LiuMingyao
 * @date : 2019/9/3 21:18
 * @description : 对于nio的buffer读写测试，只要模式切换都需要进行翻转
 * <p>Nio Buffer属性如下:
 * private int mark = -1;
 * private int position = 0;
 * private int limit;
 * private int capacity;
 * </p>
 */
public class NioTest1 {
    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(10);

        for (int i = 0; i < 10; i++) {
            int num = new SecureRandom().nextInt(20);//比传统的Random更安全
            intBuffer.put(num);
        }

        intBuffer.flip();

        for (int i = 0; i < 10; i++) {
            System.out.println(intBuffer.get(i));
        }
    }
}