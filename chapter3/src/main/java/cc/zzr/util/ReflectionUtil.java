package cc.zzr.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class ReflectionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);


    public static Object getInstance(Class<?> cls) {
        Object instance;
        try {
            instance = cls.newInstance();
        } catch (Exception e) {
            LOGGER.error("get instance failure", e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    public static Object invokeMethod(Object obj, Method method, Object[] params) {
        Object result;
        try {
            method.setAccessible(true);
            result = method.invoke(obj, params);
        } catch (Exception e) {
            LOGGER.error("invoke method failure ", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    public static void setField(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            LOGGER.error("set field failure", e);
            throw new RuntimeException(e);
        }
    }
}
