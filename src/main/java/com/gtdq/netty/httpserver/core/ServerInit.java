package com.gtdq.netty.httpserver.core;

import com.gtdq.netty.httpserver.server.HttpServer;
import com.gtdq.netty.utils.ExceptionUtil;
import com.gtdq.netty.utils.LogUtil;
import com.gtdq.netty.utils.ObjUtil;

/**
 * @author : LiuMingyao
 * @date : 2019/8/31 18:04
 * @description : TODO
 */

public class ServerInit {
    public static void init() {
        //资源解析
        ObjUtil.getSingletonObj(UriMappingAnalysis.class).initMapping();
        try {
            //启动http服务器
            HttpServer.start();
        } catch (Exception e) {
            LogUtil.errorLog("HTTP服务器启动失败." + ExceptionUtil.appendExceptionInfo(e));
        }
    }
}