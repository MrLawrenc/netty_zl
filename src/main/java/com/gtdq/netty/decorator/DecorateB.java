package com.gtdq.netty.decorator;

/**
 * @author : LiuMingyao
 * @date : 2019/9/3 20:18
 * @description : 具体的装饰器，复写父类方法(父类实现了需要被装饰的类)
 */
public class DecorateB extends FilterInputStream {
    public DecorateB(InputStream in) {
        super(in);
    }

    @Override
    public void doSomething() {
        super.doSomething();
        doB();
    }

    protected void doB() {
        System.out.println("做B.......................");
    }
}