package org.stcs.server.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.PARAMETER, ElementType.METHOD})
public @interface JSONValidation {

    String schemaName();

    String message() default "Json数据校验失败";
}
