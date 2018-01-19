package cc.zzr.util;

public final class CastUtil
{
    public static String castString(Object obj)
    {
        return castString(obj, "");
    }

    public static String castString(Object obj, String defaultValue)
    {
        return obj != null ? String.valueOf(obj) : defaultValue;
    }

    public static double castDouble(Object obj)
    {
        return castDouble(obj, 0.0D);
    }

    public static double castDouble(Object obj, double defaultValue)
    {
        double value = defaultValue;
        if (obj != null) {
            String stringValue = castString(obj);
            if (StringUtil.isNotEmpty(stringValue)) {
                try {
                    value = Double.parseDouble(stringValue);
                } catch (NumberFormatException e) {
                    value = defaultValue;
                }
            }
        }
        return value;
    }

    public static long castLong(Object obj)
    {
        return castLong(obj, 0L);
    }

    public static long castLong(Object obj, long defaultValue)
    {
        long value = defaultValue;
        if (obj != null) {
            String stringValue = castString(obj);
            if (StringUtil.isNotEmpty(stringValue)) {
                try {
                    value = Long.parseLong(stringValue);
                } catch (NumberFormatException e) {
                    value = defaultValue;
                }
            }
        }
        return value;
    }

    public static int castInt(Object obj)
    {
        return castInt(obj, 0);
    }

    public static int castInt(Object obj, int defaultValue)
    {
        int value = defaultValue;
        if (obj != null) {
            String stringValue = castString(obj);
            if (StringUtil.isNotEmpty(stringValue)) {
                try {
                    value = Integer.parseInt(stringValue);
                } catch (NumberFormatException e) {
                    value = defaultValue;
                }
            }
        }
        return value;
    }

    public static boolean castBoolean(Object obj)
    {
        return castBoolean(obj, false);
    }

    public static boolean castBoolean(Object obj, boolean defaultValue)
    {
        boolean booleanValue = defaultValue;
        if (obj != null) {
            booleanValue = Boolean.parseBoolean(castString(obj));
        }
        return booleanValue;
    }
}
