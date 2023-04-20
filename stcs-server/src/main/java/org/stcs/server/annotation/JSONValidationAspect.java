package org.stcs.server.annotation;

import java.io.InputStream;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONTokener;
import org.springframework.stereotype.Component;

/**
 * @author zhouxiang
 * @describe
 * @create 2021/10/20 14:47
 * @since 1.0.0
 */

@Component
@Aspect
@Slf4j
public class JSONValidationAspect {

    @Pointcut("@annotation(org.stcs.server.annotation.JSONValidation)")
    private void pointcut() {

    }

    @Before("pointcut() && @annotation(JSONValidation)")
    public void before(JoinPoint joinPoint, JSONValidation JSONValidation) {
        Object[] args = joinPoint.getArgs();
        if (args != null) {
            String jsonString = JSONObject.toJSONString(args[0]);
            /** 验证的结果，可以返回，也可以写个全局捕捉的异常 */
            String validMsg = validJson(jsonString, JSONValidation.schemaName());
        }
        log.info("数据：" + JSONObject.toJSONString(args) + JSONValidation.schemaName());
    }


    /**
     * 使用开源工具https://github.com/java-json-tools/json-schema-validator校验json
     *
     * @param jsonString 被校验数据
     * @param schemaName 校验schema名称
     * @return 返回失败消息，为空则校验成功
     */
    public String validJson(String jsonString, String schemaName) {
        StringBuilder sBuilder = new StringBuilder();
        try {
            //InputStream inputStream = getClass().getResourceAsStream("/schema/hello.json");

            //org.json.JSONObject rawSchema = new org.json.JSONObject(new JSONTokener(inputStream));

            org.json.JSONObject rawSchema = new org.json.JSONObject(jsonString);
            InputStream in1 = getClass().getResourceAsStream("/schema/" + schemaName);
            org.json.JSONObject sSchema = new org.json.JSONObject(new JSONTokener(in1));
            Schema schema = SchemaLoader.load(sSchema);
            schema.validate(rawSchema);

        } catch (ValidationException e) {
            log.error(e.getMessage());
            sBuilder.append(e.getMessage());
        }

        return sBuilder.toString();

    }

}

