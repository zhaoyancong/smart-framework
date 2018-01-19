package cc.zzr.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(StringUtil.class);

    public static final String SEPARATOR = String.valueOf((char) 29);

    public static boolean isEmpty(String str) {
        if (str != null) {
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String[] splitString(String source, String splitChar) {
        String[] result = null;
        try {
            if (isNotEmpty(source))
                result = source.split(splitChar);
        } catch (Exception e) {
            LOGGER.error("split string failure", e);
            throw new RuntimeException(e);
        }
        return result;
    }
}
