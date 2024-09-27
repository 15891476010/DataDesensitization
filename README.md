## 导入数据脱敏依赖
```maven
    <dependency>
        <groupId>cn.tzhyys</groupId>
        <artifactId>DataDesensitization</artifactId>
        <version>1.0.3</version>
    </dependency>
```

## 1、在项目config文件夹中创建SensitiveDataAutoConfiguration文件
## 2、进行全局注册
```java
import cn.tzhyys.datadesensitization.SensitiveDataAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SensitiveDataAutoConfiguration {

    @Bean
    public SensitiveDataAspect sensitiveDataAspect() {
        return new SensitiveDataAspect();
    }
}
```

## 在实体类上写入注解，当前只支持身份证号、邮箱、手机号
```java
    @Sensitive(type = DesensitizationType.PHONE)
    private String phoneNumber;
```
## 注：如果你有Vo实体返回类等，一系列的转换操作须在service层进行操作才能触发脱敏