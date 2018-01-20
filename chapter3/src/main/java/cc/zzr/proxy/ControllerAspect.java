package cc.zzr.proxy;


import cc.zzr.annotation.Aspect;
import cc.zzr.annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);
    private long begin;

    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
        LOGGER.debug("-------begin----------");
        LOGGER.debug(String.format("class:%s", new Object[]{cls.getName()}));
        LOGGER.debug(String.format("method:%s", new Object[]{method.getName()}));
        this.begin = System.currentTimeMillis();
    }

    public void after(Class<?> cls, Method method, Object[] params) throws Throwable {
        LOGGER.debug(String.format("time:%s", new Object[]{Long.valueOf(System.currentTimeMillis() - this.begin)}));
        LOGGER.debug("-------end----------");
    }
}