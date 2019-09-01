package com.gtdq.netty.utils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : LiuMingyao
 * @date : 2019/7/24 14:12
 * @description : 对象相关操作
 */
public class ObjUtil {


    /**
     * @author : LiuMing
     * @date : 2019/7/24 14:23
     * @description :   将oldObj的所有属性值赋给newObj对应的属性（字段名一致的全部复制）
     * <p>isStrict：是否严格匹配</p>
     */
    public static boolean propertyCopy(Object oldObj, Object newObj, boolean isStrict) {

        //得到所有属性
        Field[] fields = oldObj.getClass().getDeclaredFields();
        Field[] newFields = newObj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {//遍历
            for (int j = 0; j < newFields.length; j++) {
                try {
                    //得到属性
                    Field field = fields[i];
                    //打开私有访问
                    field.setAccessible(true);
                    //获取属性名
                    String name = field.getName();

                    Field newField = newFields[j];
                    newField.setAccessible(true);
                    String newFieldName = newField.getName();
                    //属性名一致才复制属性值
                    if (name.equals(newFieldName)) {
                        //获取属性值
                        Object value = field.get(oldObj);
                        newField.set(newObj, value);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("复制属性值失败！");
                }
            }

        }
        return true;
    }

    //==============================================================================================================
    //单例对象池
    private static Map<String, Object> pool = new ConcurrentHashMap<>();

    /**
     * @author : LiuMing
     * @date : 2019/7/24 14:58
     * @description :   获取单例对象（只支持无参构造）
     */
    @SuppressWarnings("unchecked")
    public static <T> T getSingletonObj(Class<T> clazz) {
        final String key = clazz.getName();
        T obj = (T) pool.get(key);
        if (null == obj) {
            synchronized (clazz) {
                //再次判断
                obj = (T) pool.get(key);
                if (null == obj) {
                    //创建对象
                    try {
                        T t = clazz.getDeclaredConstructor().newInstance();
                        pool.put(key, t);
                        return t;
                    } catch (Exception e) {
                        throw new RuntimeException("创建单例对象失败,可能是没有无参构造器(修饰符无关)！", e);
                    }
                }
            }
        }
        return obj;
    }

    /**
     * @author : LiuMing
     * @date : 2019/7/24 16:02
     * @description :   从单例池中移除指定单例对象
     */
    public static void removeSingletonObj(Class<?> clazz) {
        if (null != clazz) {
            pool.remove(clazz.getName());
        }
    }

    /**
     * @author : LiuMing
     * @date : 2019/7/24 16:05
     * @description :   清除单例池的所有对象
     */
    public static void destroy() {
        pool.clear();
    }
    //==============================================================================================================


}