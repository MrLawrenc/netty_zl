package com.gtdq.netty.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author : LiuMingyao
 * @date : 2019/9/3 21:29
 * @description : TODO
 */
public class NioTest3 {
    public static void main(String[] args) throws Exception {
        try (FileOutputStream outputStream = new FileOutputStream("NioTest3.txt")) {

            FileChannel channel = outputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            byte[] bytes = "HELLO WORLD!".getBytes();

            //1.先写入buffer
            for (int i = 0; i < bytes.length; i++) {
                byteBuffer.put(bytes[i]);
            }
            //2.切换模式,channel读取写入buffer的数据
            byteBuffer.flip();
            channel.write(byteBuffer);
        }
    }
}