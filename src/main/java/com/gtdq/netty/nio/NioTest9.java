package com.gtdq.netty.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author : LiuMingyao
 * @date : 2019/9/5 20:57
 * @description : 直接操作内存
 * <p>
 * * <p> Mapped byte buffers are created via the {@link java.nio.channels.FileChannel#map FileChannel.map} method.  This class
 * * extends the {@link java.nio.ByteBuffer} class with operations that are specific to
 * * memory-mapped file regions.
 */
public class NioTest9 {
    public static void main(String[] args) throws Exception {

        try (RandomAccessFile accessFile = new RandomAccessFile("NioTest9.txt", "rw");) {
            FileChannel channel = accessFile.getChannel();
            MappedByteBuffer mapByffer   = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
            mapByffer.put(0, (byte)'a');
            mapByffer.put(3, (byte)'b');
//            mapByffer.put(2, (byte)'c');
        }
    }
}