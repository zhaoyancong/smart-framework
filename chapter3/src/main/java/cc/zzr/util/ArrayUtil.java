package cc.zzr.util;


import org.apache.commons.lang3.ArrayUtils;

public final class ArrayUtil {

    /**
     * 判断数组是否为空
     * @param array
     * @return
     */
    public static final boolean isEmpty(Object[] array) {
        return ArrayUtils.isEmpty(array);
    }

    /**
     * 判断数据是否为非空
     * @param array
     * @return
     */
    public static final boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }
}