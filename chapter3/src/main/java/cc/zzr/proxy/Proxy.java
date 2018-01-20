package cc.zzr.proxy;

public interface Proxy {
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
