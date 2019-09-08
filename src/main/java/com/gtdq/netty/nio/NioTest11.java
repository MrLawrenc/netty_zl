package com.gtdq.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author : LiuMingyao
 * @date : 2019/9/5 21:08
 * @description : buffer的聚合和拆分
 */
public class NioTest11 {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(8899);

        socketChannel.socket().bind(address);


        int size = 2 + 3 + 4;
        ByteBuffer[] buffers = new ByteBuffer[3];
        buffers[0] = ByteBuffer.allocate(2);
        buffers[1] = ByteBuffer.allocate(3);
        buffers[2] = ByteBuffer.allocate(4);

        SocketChannel s = socketChannel.accept();//等待连接
        while (true) {
            int byteRead = 0;
            while (byteRead < size) {
                long read = s.read(buffers);
                byteRead += read;

                System.out.println("byteRead:" + byteRead);
            }

            Arrays.asList(buffers).stream().forEach(buffer -> {
                buffer.flip();
            });

            int byteWrite = 0;
            while (byteWrite < byteRead) {
                long write = s.write(buffers);
                byteWrite += write;
                System.out.println("write:" + byteWrite);
            }


            Arrays.asList(buffers).forEach(buffer -> {
                buffer.clear();
            });
        }
    }
}