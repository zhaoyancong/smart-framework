package cc.zzr.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.Collection;
import java.util.Map;

public final class CollectionUtil {

    /**
     * 判断集合为空
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection){
            return CollectionUtils.isEmpty(collection);
    }

    /**
     * 判断集合不为空
     * @param collection
     * @return
     */
    public static boolean isNotEmpty(Collection<?> collection){
        return !isEmpty(collection);
    }

    /**
     * 判断Map为空
     * @param map
     * @return
     */
    public static  boolean isEmpty(Map<?,?> map){
        return MapUtils.isEmpty(map);
    }

    /**
     * 判断map不为空
     * @param map
     * @return
     */
    public static boolean isNotEmpty(Map<?,?> map){
        return  !isEmpty(map);
    }
}
