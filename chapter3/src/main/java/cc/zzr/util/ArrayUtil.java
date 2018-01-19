package cc.zzr.util;


import org.apache.commons.lang3.ArrayUtils;

public final class ArrayUtil {

    public static final boolean isEmpty(Object[] array) {
        return ArrayUtils.isEmpty(array);
    }

    public static final boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }
}