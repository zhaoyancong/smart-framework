package cc.zzr.helper;

import cc.zzr.annotation.Inject;
import cc.zzr.util.ArrayUtil;
import cc.zzr.util.CollectionUtil;
import cc.zzr.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 依赖注入助手类
 */
public final class IocHelper {
    static {
        //获取所有bean类与bean实例之间的映射关系
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap))
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                //从beanMap中获取bean类与bean实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                //获取bean实例中所有成员变量
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(beanFields))
                    //遍历bean field
                    for (Field field : beanFields)
                        //判断bean field是否带有Inject注解
                        if (field.isAnnotationPresent(Inject.class)) {
                            //从bean map 中获取bean field对应的实例
                            Class<?> beanFieldClass = field.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if (beanFieldInstance != null)
                                //通过反射初始化BeanField的值
                                ReflectionUtil.setField(beanInstance, field, beanFieldInstance);
                        }
            }
    }
}
