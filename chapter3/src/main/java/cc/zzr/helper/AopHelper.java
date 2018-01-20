package cc.zzr.helper;

import cc.zzr.annotation.Aspect;
import cc.zzr.annotation.Service;
import cc.zzr.proxy.AspectProxy;
import cc.zzr.proxy.Proxy;
import cc.zzr.proxy.ProxyManager;
import cc.zzr.proxy.TransactionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public final class AopHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

    static {
        try {
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> targetEntity : targetMap.entrySet()) {
                Class<?> targetClass = targetEntity.getKey();
                List<Proxy> proxyList = targetEntity.getValue();
                Object proxy = ProxyManager.createProxy(targetClass, proxyList);
                BeanHelper.setBean(targetClass, proxy);
            }
        } catch (Exception e) {
            LOGGER.error("aop failure", e);
        }
    }

    private static Set<Class<?>> createTargetClassSet(Aspect aspect)
            throws Exception {
        Set targetClassSet = new HashSet();
        Class annotation = aspect.value();
        if ((annotation != null) && (annotation.equals(Aspect.class))) {
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }

    private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Exception {
        Map proxyMap = new HashMap();
        addAspectProxy(proxyMap);
        addTransactionProxy(proxyMap);
        return proxyMap;
    }

    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Map<Class<?>,List<Proxy>> targetMap = new HashMap();
        Class proxyClass;
        for (Map.Entry<Class<?>,Set<Class<?>>> entry : proxyMap.entrySet()) {
            proxyClass = entry.getKey();
            Set<Class<?>> targetClassSet = entry.getValue();
            for (Class targetClass : targetClassSet) {
                Proxy proxy = (Proxy) proxyClass.newInstance();
                if (targetMap.containsKey(proxy)) {
                    targetMap.get(targetClass).add(proxy);
                } else {
                    List<Proxy> proxyList = new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass, proxyList);
                }
            }
        }
        return targetMap;
    }

    private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        for (Class cls : proxyClassSet)
            if (cls.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = (Aspect) cls.getAnnotation(Aspect.class);
                Set targetClassSet = createTargetClassSet(aspect);
                proxyMap.put(cls, targetClassSet);
            }
    }

    private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
        Set serviceClassSet = ClassHelper.getClassSetByAnnotation(Service.class);
        proxyMap.put(TransactionProxy.class, serviceClassSet);
    }


}
