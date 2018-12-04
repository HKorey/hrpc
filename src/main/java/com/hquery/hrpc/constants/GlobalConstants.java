package com.hquery.hrpc.constants;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author hquery.huang
 * 2018/5/3 16:48
 */
public class GlobalConstants {

    private static Unsafe UNSAFE = null;

    public static final String THE_UNSAFE = "theUnsafe";

    static {
        Field f = null;
        try {
            f = Unsafe.class.getDeclaredField(THE_UNSAFE);
            f.setAccessible(true);
            UNSAFE = (Unsafe) f.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Field getGlobalMapField() {
        try {
            return GlobalConstants.class.getDeclaredField("globalMap");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long getFieldOffset(Field field) {
        if (field == null) {
            return 0L;
        }
        return UNSAFE.staticFieldOffset(field);
    }

    public Object getStaticFieldBase(Field field) {
        if (field == null) {
            return null;
        }
        return UNSAFE.staticFieldBase(field);
    }

}
