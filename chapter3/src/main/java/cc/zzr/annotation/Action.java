package cc.zzr.annotation;

import java.lang.annotation.*;

/**
 * Action 方法注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    /**
     * 请求类型和参数
     * @return
     */
    String value();
}
