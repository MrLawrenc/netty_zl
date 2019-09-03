package com.gtdq.netty.decorator;

/**
 * @author : LiuMingyao
 * @date : 2019/9/3 20:10
 * @description : 装饰类的顶层接口，在io模型中，大量使用装饰器模式，过滤流
 * @see java.io.FilterInputStream
 * 就是顶层的装饰器接口,里面持有节点流的对象，而节点流的顶层接口就是
 * @see java.io.InputStream
 * <p>装饰器是对于某一个具体的对象而言，而继承是对于整个类而言的</p>
 */
public class FilterInputStream implements InputStream {
    protected InputStream in;

    //通过构造器注入需要被装饰的对象
    public FilterInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public void doSomething() {
        in.doSomething();
    }

}