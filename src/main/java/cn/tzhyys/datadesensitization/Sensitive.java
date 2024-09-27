package cn.tzhyys.datadesensitization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
/*
  自定义注解
 */
public @interface Sensitive {
    DesensitizationType type(); // 使用枚举类型指定脱敏类型
}
