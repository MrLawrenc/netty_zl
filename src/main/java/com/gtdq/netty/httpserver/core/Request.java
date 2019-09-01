package com.gtdq.netty.httpserver.core;

import com.gtdq.netty.utils.ExceptionUtil;
import com.gtdq.netty.utils.LogUtil;
import com.gtdq.netty.utils.ObjUtil;
import com.gtdq.netty.utils.ParamUtil;
import io.netty.handler.codec.http.FullHttpResponse;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author : LiuMingyao
 * @date : 2019/8/31 23:16
 * @description : TODO
 */
public class Request {

    /**
     * @author : LiuMing
     * @date : 2019/9/1 0:47
     * @description :  根据请求的url和初始化保存的uri对比，如果找到了就根据反射调用相应的方法，然后返回response
     */
    public static FullHttpResponse get(String uri, Map<String, Map<Method, Object>> uriMap) {
        String[] uriAndParams = uri.split("\\?");
        String uriPath = uriAndParams[0];
        Map<String, String> paramMap = null;
        if (uriAndParams.length > 1) {
            paramMap = getParams(uriAndParams[1]);
        }
        return invokeByUri(uriMap, paramMap, uriPath);
    }

    /**
     * @author : LiuMing
     * @date : 2019/9/1 0:49
     * @description :  获取HTTP GET请求的参数(key为参数名value为参数值)
     */
    private static Map<String, String> getParams(String paramStr) {
        String[] params = paramStr.split("&");
        int length = params.length;
        Map<String, String> paramMap = new HashMap<>(length);
        for (int i = 0; i < length; i++) {
            String[] param = params[i].split("=");
            paramMap.put(param[0], param[1]);
        }
        return paramMap;
    }

    /**
     * @author : LiuMing
     * @date : 2019/9/1 0:50
     * @description :  根据uri规则调用相应的方法反射，并返回一个response
     */
    public static FullHttpResponse invokeByUri(Map<String, Map<Method, Object>> uriMap, Map<String, String> paramMap, String uriPath) {
        for (String uriStr : uriMap.keySet()) {
            Map<Method, Object> map = uriMap.get(uriStr);
            if (uriStr.equals(uriPath)) {
                Method method = map.keySet().iterator().next();
                try {
                    String[] params = null;
                    if (null != paramMap) {//有参数的get请求
                        params = getMethodParams(paramMap, map.get(method), method);
                    }
                    Object o = method.invoke(map.get(method), params);
                    if (Objects.isNull(o)) {
                        o = "方法无返回";
                    }
                    Response res = ObjUtil.getSingletonObj(Response.class);
                    FullHttpResponse response = res.getOkResponse(o.toString());
                    return response;
                } catch (Exception e) {
                    LogUtil.errorLog("=========" + ExceptionUtil.appendExceptionInfo(e));
                }
            }
        }
        return null;
    }

    /**
     * @param paramMap get请求传递过来的参数,key为参数名,value为参数值
     * @author : LiuMing
     * @date : 2019/9/1 0:33
     * @description :  获取方法method的所有参数
     * <p>如请求http://localhost:9527/test?id=2</p>那么paramMap为{id=2}
     * <p>obj为方法method所在类的对象</p>
     * <p>通过获取method的参数名来和get请求的参数名对比，如果一样，就将值封装到params集合返回</p>
     */
    private static String[] getMethodParams(Map<String, String> paramMap, Object obj, Method method) {
        String[] params = new String[paramMap.size()];
        String[] paramNames = ParamUtil.getParamNames(obj.getClass(), method.getName());
        for (int i = 0; i < paramNames.length; i++) {
            String realParamName = paramNames[i];
            for (String paramName : paramMap.keySet()) {
                if (realParamName.equals(paramName)) {
                    params[i] = paramMap.get(paramName);
                }
            }
        }
        return params;
    }
}