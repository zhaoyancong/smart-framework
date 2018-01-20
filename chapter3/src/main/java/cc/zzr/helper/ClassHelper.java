package cc.zzr.helper;

import cc.zzr.annotation.Controller;
import cc.zzr.annotation.Service;
import cc.zzr.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * 类操作助手类
 */
public final class ClassHelper {

    /**
     * 定义类集合（用于存放所加载的类）
     */
    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    /**
     * 获取应用包下的所有类
     *
     * @return
     */
    public static final Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }


    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass) {
        Set classSet = new HashSet();
        for (Class cls : CLASS_SET) {
            if ((superClass.isAssignableFrom(cls)) && (!superClass.equals(cls))) {
                classSet.add(cls);
            }
        }
        return classSet;
    }

    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass) {
        Set classSet = new HashSet();
        for (Class cls : CLASS_SET) {
            if (cls.isAnnotationPresent(annotationClass)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包下的所有service
     *
     * @return
     */
    public static final Set<Class<?>> getServiceClassSet() {
        Set classSet = new HashSet();
        for (Class cls : CLASS_SET) {
            if (cls.isAnnotationPresent(Service.class)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包下的所有controller类
     *
     * @return
     */
    public static final Set<Class<?>> getControllerClassSet() {
        Set classSet = new HashSet();
        for (Class cls : CLASS_SET) {
            if (cls.isAnnotationPresent(Controller.class)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包下的所有bean类（包括service、controller）
     *
     * @return
     */
    public static final Set<Class<?>> getBeanClassSet() {
        Set classSet = new HashSet();
        classSet.addAll(getServiceClassSet());
        classSet.addAll(getControllerClassSet());
        return classSet;
    }


}
