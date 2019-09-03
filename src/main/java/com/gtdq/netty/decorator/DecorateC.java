package com.gtdq.netty.decorator;

/**
 * @author : LiuMingyao
 * @date : 2019/9/3 20:21
 * @description : TODO
 */
public class DecorateC extends FilterInputStream {
    public DecorateC(InputStream in) {
        super(in);
    }

    @Override
    public void doSomething() {
        super.doSomething();
        doC();
    }

    protected void doC() {
        System.out.println("做C.......................");
    }
}