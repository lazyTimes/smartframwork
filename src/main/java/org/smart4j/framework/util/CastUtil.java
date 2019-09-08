package org.smart4j.framework.util;

/**
 * 数据类型转换使用的工具类
 */
public class CastUtil {

    /**
     * 转为String
     *
     * @param obj 对象
     * @return
     */
    public static String castString(Object obj) {
        return CastUtil.castString(obj, "");
    }

    /**
     * 转为String 类型(带有默认值)
     *
     * @param obj           对象
     * @param defualtValues 默认值
     * @return
     */
    public static String castString(Object obj, String defualtValues) {
        return obj != null ? String.valueOf(obj) : defualtValues;
    }

    /**
     * 转为double类型
     *
     * @param obj 对象
     * @return
     */
    public static double castDouble(Object obj) {
        return CastUtil.castDoube(obj, 0.0f);
    }

    /**
     * 转为double(带有默认值)
     *
     * @param obj          对象
     * @param defualtValue 默认值
     * @return
     */
    public static double castDoube(Object obj, double defualtValue) {
        double doubleValue = defualtValue;
        if (obj != null) {
            String strValue = castString(obj);
            if (MyStringUtil.isNotEmpty(strValue)) {
                try {
                    defualtValue = Double.parseDouble(strValue);
                } catch (NumberFormatException format) {
                    doubleValue = defualtValue;
                }
            }
        }
        return doubleValue;
    }

    /**
     * 转为long型数据
     *
     * @param obj 对象
     * @return
     */
    public static long castLong(Object obj) {
        return CastUtil.castLong(obj, 0L);
    }


    /**
     * 转为long型数据 (带有默认值)
     *
     * @param obj          对象
     * @param defualtValue 默认值
     * @return
     */
    public static long castLong(Object obj, long defualtValue) {
        long longValue = defualtValue;
        if (obj != null) {
            String strValue = castString(obj);
            if (MyStringUtil.isNotEmpty(strValue)) {
                try {
                    defualtValue = Long.parseLong(strValue);
                } catch (NumberFormatException format) {
                    longValue = defualtValue;
                }
            }
        }
        return longValue;
    }


    /**
     * 转为int
     * @param obj 对象
     * @return
     */
    public static int castInt(Object obj) {
        return CastUtil.castInt(obj, 0);
    }

    /**
     * 转为int(带有默认值)
     * @param obj 对象
     * @param defualtValue 默认值
     * @return
     */
    public static int castInt(Object obj, int defualtValue) {
        int intValue = defualtValue;
        if (obj != null) {
            String strValue = castString(obj);
            if (MyStringUtil.isNotEmpty(strValue)) {
                try {
                    defualtValue = Integer.parseInt(strValue);
                } catch (NumberFormatException format) {
                    intValue = defualtValue;
                }
            }
        }
        return intValue;
    }

    public static boolean castBoolean(Object obj, boolean defualtValue){
        boolean booleanValue = defualtValue;
        if( obj == null){
            booleanValue = Boolean.parseBoolean(castString(obj));
        }
        return booleanValue;
    }

    public static boolean castBoolean(Object obj){
        return castBoolean(obj, false);
    }


}
