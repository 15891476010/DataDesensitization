package cn.tzhyys.datadesensitization;

import java.lang.reflect.Field;

public class DataDesensitizer {
    public static void desensitize(Object obj) throws IllegalAccessException {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Sensitive.class)) {
                field.setAccessible(true);
                String originalValue = (String) field.get(obj);
                String maskedValue = mask(originalValue, field.getAnnotation(Sensitive.class).type());
                field.set(obj, maskedValue);
            }
        }
    }

    private static String mask(String value, DesensitizationType type) {
        if (value == null) return null;

        switch (type) {
            case PHONE:
                return value.replaceAll("(\\d{3})(\\d{4})(\\d+)", "$1****$3"); // 替换中间4位
            case EMAIL:
                return value.replaceAll("(?<=.{2}).(?=.*@)", "*"); // 隐藏邮箱中间部分
            case CARD:
                return value.replaceAll("(?<=\\d{4})(\\d{4})(\\d{4})(\\d+)", "****$4"); // 替换中间部分
            default:
                return value.replaceAll(".", "*"); // 默认替换为星号
        }
    }
}
