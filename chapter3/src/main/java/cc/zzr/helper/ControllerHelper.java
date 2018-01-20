package cc.zzr.helper;

import cc.zzr.annotation.Action;
import cc.zzr.bean.Handler;
import cc.zzr.bean.Request;
import cc.zzr.util.ArrayUtil;
import cc.zzr.util.CollectionUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * controller操作助手
 */
public final class ControllerHelper {


    /**
     * 存放Request和Handler的映射关系（Action Map)
     */
    private static final Map<Request, Handler> ACTION_MAP = new HashMap();


    static {
        //获取所有controller类
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (CollectionUtil.isNotEmpty(controllerClassSet)) {
            //遍历controller类
            for (Class<?> controllerClass : controllerClassSet) {
                //获取controller中定义的方法
                Method[] methods = controllerClass.getDeclaredMethods();
                if (ArrayUtil.isNotEmpty(methods))
                    //遍历controller中的方法
                    for (Method method : methods)
                        //判断当前方法是否有Action注解
                        if (method.isAnnotationPresent(Action.class)) {
                            Action action = method.getAnnotation(Action.class);
                            //从Action中获取URL映射规则
                            String mapping = action.value();
                            //验证URL映射规则
                            if (mapping.matches("\\w+:/\\w*")) {
                                String[] array = mapping.split(":");
                                if ((ArrayUtil.isNotEmpty(array)) && (array.length == 2)) {
                                    //判断请求的方法和路径
                                    String requestMethod = array[0];
                                    String requestPath = array[1];
                                    Request request = new Request(requestMethod, requestPath);
                                    Handler handler = new Handler(controllerClass, method);
                                    ACTION_MAP.put(request, handler);
                                }
                            }
                        }
            }
        }

    }

    /**
     * 获取Handler
     *
     * @param requstMethod
     * @param requestPath
     * @return
     */
    public static Handler getHandler(String requstMethod, String requestPath) {
        Request request = new Request(requstMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}
