package cc.zzr.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

public class ProxyChain {
    private final Class<?> targetClass;
    private final Object targetObject;
    private final Method targetMethod;
    private final MethodProxy methodProxy;
    private final Object[] methodParams;
    private List<Proxy> proxyList;
    private int proxyIndex = 0;

    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList = proxyList;
    }

    public Class<?> getTargetClass() {
        return this.targetClass;
    }

    public Method getTargetMethod() {
        return this.targetMethod;
    }

    public Object[] getMethodParams() {
        return this.methodParams;
    }

    public Object doProxyChain() throws Throwable {
        Object methodResult;
        if (this.proxyIndex < this.proxyList.size())
            methodResult = (this.proxyList.get(this.proxyIndex++)).doProxy(this);
        else {
            methodResult = this.methodProxy.invokeSuper(this.targetObject, this.methodParams);
        }
        return methodResult;
    }
}
