package cn.tzhyys.datadesensitization;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * Sensitive data processing facets
 */
@Aspect
@Component
public class SensitiveDataAspect {

    /**
     * Processing return result
     * @param result
     * @throws IllegalAccessException
     */
    @AfterReturning(pointcut = "execution(* *..service..*(..))", returning = "result")
    public void afterReturning(Object result) throws IllegalAccessException {
        if (result != null) {
            handleSensitiveFields(result);
        }
    }

    /**
     * Handling sensitive fields
     * @param result
     * @throws IllegalAccessException
     */
    private void handleSensitiveFields(Object result) throws IllegalAccessException {
        // 遍历对象中的所有字段，查找 @Sensitive 注解
        for (Field field : result.getClass().getDeclaredFields()) {
            Sensitive sensitive = field.getAnnotation(Sensitive.class);
            if (sensitive != null) {
                field.setAccessible(true);
                Object fieldValue = field.get(result);
                if (fieldValue instanceof String) {
                    // 根据注解类型进行脱敏处理
                    String maskedValue = mask((String) fieldValue, sensitive.type());
                    field.set(result, maskedValue);
                }
            }
        }
    }

    /**
     * Desensitization according to type
     * @param value
     * @param type
     * @return
     */
    private String mask(String value, DesensitizationType type) {
        switch (type) {
            case PHONE:
                return value.replaceAll("(\\d{3})(\\d{4})(\\d+)", "$1****$3");
            case EMAIL:
                return value.replaceAll("(?<=.{2}).(?=.*@)", "*");
            case CARD:
                return value.replaceAll("(?<=\\d{4})\\d(?=\\d{4})", "*");
            default:
                return value.replaceAll(".", "*");
        }
    }
}
