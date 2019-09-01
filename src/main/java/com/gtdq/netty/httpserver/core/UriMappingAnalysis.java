package com.gtdq.netty.httpserver.core;

import com.gtdq.netty.httpserver.annotation.MarsController;
import com.gtdq.netty.httpserver.annotation.UriMapping;
import com.gtdq.netty.utils.ClassUtil;
import com.gtdq.netty.utils.ObjUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author : LiuMingyao
 * @date : 2019/8/31 16:03
 * @description : uri映射解析
 */
public class UriMappingAnalysis {

    private Map<String, Map<Method, Object>> uriMap = new HashMap<>();

    public Map<String, Map<Method, Object>> getUriMap() {
        return uriMap;
    }


    public void initMapping() {
        initMapping("com.gtdq.netty.httpserver");
    }

    /**
     * @author : LiuMing
     * @date : 2019/9/1 11:16
     * @description :  初始化uri和方法的映射关系
     */
    public void initMapping(String packageName) {
        List<Class<?>> classes = hasAnnotationClz(ClassUtil.getClasses(packageName), MarsController.class);
        for (Class<?> clz : classes) {
            Object obj = ObjUtil.getSingletonObj(clz);
            String clzUri = "";
            UriMapping uriMapping = clz.getAnnotation(UriMapping.class);
            if (Objects.nonNull(uriMapping)) {
                clzUri += uriMapping.value().strip();
            }
            List<Method> methods = hasAnnotationMethod(clz, UriMapping.class);
            for (Method method : methods) {
                String methodUri = method.getAnnotation(UriMapping.class).value();
                String uri = clzUri + methodUri;
                Map<Method, Object> map = new HashMap<>(1);
                map.put(method, obj);
                uriMap.put(uri, map);
            }
        }
    }


    /**
     * @author : LiuMing
     * @date : 2019/8/31 17:20
     * @description :   返回所有被annotationClz注解标注的类
     */
    private List<Class<?>> hasAnnotationClz(List<Class<?>> classes, Class annotationClz) {
        List<Class<?>> hasControllerClz = new ArrayList<>();
        classes.forEach(clz -> {
            for (Annotation annotation = clz.getAnnotation(annotationClz); Objects.nonNull(annotation); ) {
                hasControllerClz.add(clz);
                break;
            }
        });
        return hasControllerClz;
    }


    /**
     * @author : LiuMing
     * @date : 2019/8/31 17:34
     * @description :   返回clz下所有被annotationClz注解修饰的方法
     */
    private List<Method> hasAnnotationMethod(Class clz, Class annotationClz) {
        List<Method> hasUriMethod = new ArrayList<>();
        for (Method method : clz.getMethods()) {
            for (Object obj = method.getAnnotation(annotationClz); Objects.nonNull(obj); ) {
                hasUriMethod.add(method);
                break;
            }
        }
        return hasUriMethod;
    }

}