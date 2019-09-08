package com.gtdq.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author : LiuMingyao
 * @date : 2019/9/4 21:06
 * @description : TODO
 */
public class NioTest4 {

    public static void main(String[] args) throws Exception {
        try (FileInputStream inputStream = new FileInputStream("NioTest2.txt");) {
            FileChannel channel = inputStream.getChannel();

            FileOutputStream outputStream = new FileOutputStream("NioTest4.txt");
            FileChannel channel1 = outputStream.getChannel();

            /**
             * {@link ByteBuffer }是一个抽象类，继承了{@link Buffer }
             * {@link ByteBuffer }有5个实现类，其中四个是{@link java.nio.HeapByteBuffer}、{@link java.nio.HeapByteBufferR}、{@link java.nio.DirectByteBufferR}
             * 和{@link java.nio.DirectByteBufferR}，带R的代表只读，可以通过不带R对象的调用{@link # asReadOnlyBuffer()}方法来获得只读对象。
             *{@link java.nio.HeapByteBuffer}是在堆上开辟的内存空间，再之后进行io读取的时候，会在堆外，即操作系统copy一块内存空间，来进行io操作，
             * 而{@link java.nio.DirectByteBuffer}是操作堆外内存，在该类中，会持有一个操作系统内存的引用，在其顶层父类{@link Buffer }里面以 long address 的
             * 方式存在，该变量指向堆外内存地址，io操作是直接在堆外内存进行的，因此称为zero copy。
             * 在netty中大量使用这种方式，实现了zero copy，避免了内存拷贝，这也是高性能的一个原因
             *
             * 在创建{@link java.nio.DirectByteBuffer}对象的构造器中会调用大量的native本地方法，可以点进去看看
             */
            //============重点o拷贝=====================
            //ByteBuffer byteBuffer = ByteBuffer.allocateDirect(512);
            ByteBuffer byteBuffer = ByteBuffer.allocate(512);//开辟的数组空间是堆内存


            while (true) {
                /**
                 *注释掉会一直往NioTest4.txt文件写相同的数据。
                 * <p>
                 *     对于第一次循环来说，结果都一样，但是第一次循环，如果注释了，position和limit都是在第一次循环写完的位置(加入有10字节，那么都在索引10那个位置)，
                 *      之后执行channel.read(byteBuffer);这时候由于position和limit在同一位置，因此不会读字节，因此read一直会打印0，由于byteBuffer里面一直有之前read
                 *      的数据，因此会向文件中一直重复写这些数据。
                 *      <i>如果调用了clear，position和limit会回到初始位置</i>，即position为0，那么再次将input channel中的数据读到byteBuffer中的时候，由于第一次循环就把channel
                 *      的数据全部读完了，因此返回的read为-1，继而退出while
                 *      (以上结果是第一次数据就会全部读完的情况，即capacity>=输入流文件的字节数的)
                 * </p>
                 */
                //重点:注释掉会发生什么(需要配合allocate使用，使用的是堆内存)
                byteBuffer.clear();

                int read = channel.read(byteBuffer);
                if (read == -1) {
                    break;
                }
                System.out.println("read:" + read);
                byteBuffer.flip();
                int write = channel1.write(byteBuffer);
            }

            channel1.close();
            outputStream.close();
        }
    }
}