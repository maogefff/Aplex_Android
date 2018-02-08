package com.aplex.webcan.util;


import java.lang.reflect.ParameterizedType;

/**
 * Created by baixiaokang on 16/4/30.
 */
public class TUtil {
    //在类上约束表示作用域范围是类
    //而这里在方法上约束，表示约束范围是方法体
    //
    public static <T> T getT(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
