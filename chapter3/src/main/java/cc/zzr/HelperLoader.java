package cc.zzr;

import cc.zzr.helper.*;
import cc.zzr.util.ClassUtil;

/**
 * 加载相应的Helper类
 */
public final class HelperLoader {
    public static void init() {
        Class[] classList = {ClassHelper.class, BeanHelper.class, AopHelper.class, IocHelper.class, ControllerHelper.class};

        for (Class cls : classList)
            ClassUtil.loadClass(cls.getName(), true);
    }
}
