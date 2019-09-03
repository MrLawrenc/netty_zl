package com.gtdq.netty.httpserver.core;

import com.gtdq.netty.utils.ExceptionUtil;
import com.gtdq.netty.utils.LogUtil;
import com.gtdq.netty.utils.ObjUtil;
import com.gtdq.netty.utils.ParamUtil;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author : LiuMingyao
 * @date : 2019/8/31 23:16
 * @description : TODO
 */
public class Request {


    private static Map<String, Map<Method, Object>> uriMap = ObjUtil.getSingletonObj(UriMappingAnalysis.class).getUriMap();

    /**
     * @author : LiuMing
     * @date : 2019/9/1 0:47
     * @description :  根据请求的url和初始化保存的uri对比，如果找到了就根据反射调用相应的方法，然后返回response
     */
    public static FullHttpResponse get(String uri) {
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

    /**
     * @author : LiuMing
     * @date : 2019/9/3 16:06
     * @description :   获取post请求的response
     */
    public static FullHttpResponse post(HttpRequest request) {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) request;
            HashMap<String, String> paramMap = postParams(fullRequest);
            return invokeByUri(uriMap, paramMap, request.uri());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Deprecated
    public void postParam(FullHttpRequest fullRequest) {
        byte[] bytes = new byte[1024];
        ByteBuf byteBuf = fullRequest.content().readBytes(bytes);
        String content = new String(bytes, Charset.defaultCharset());
        System.out.println(byteBuf);
        LogUtil.infoLog("post请求的数据为:{}", content);
    }

    /**
     * @author : LiuMing
     * @date : 2019/9/3 16:06
     * @description :  获取post请求的参数
     */
    private static HashMap<String, String> postParams(FullHttpRequest fullRequest) throws IOException {
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(fullRequest);
        decoder.offer(fullRequest);
        List<InterfaceHttpData> paramList = decoder.getBodyHttpDatas();
        HashMap<String, String> paramMap = new HashMap<>();
        for (InterfaceHttpData param : paramList) {
            Attribute data = (Attribute) param;
            paramMap.put(data.getName(), data.getValue());
        }
        return paramMap;
    }
}