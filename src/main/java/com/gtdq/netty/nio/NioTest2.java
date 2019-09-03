package com.gtdq.netty.nio;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author : LiuMingyao
 * @date : 2019/9/3 21:23
 * @description : TODO
 */
public class NioTest2 {
    public static void main(String[] args) throws Exception {
        try (FileInputStream inputStream = new FileInputStream("NioTest2.txt")) {
            FileChannel channel = inputStream.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            //1.channel将输入流写到buffer中
            channel.read(byteBuffer);
            //2.写->切换到读，读取buffer中的数据
            byteBuffer.flip();

            while (byteBuffer.hasRemaining()) {//or byteBuffer.remaining()>0
                System.out.println((char) byteBuffer.get());
            }
            for (int i = 0; i < byteBuffer.limit(); i++) {
                System.out.println((char) byteBuffer.get(i));
            }

        }
    }
}