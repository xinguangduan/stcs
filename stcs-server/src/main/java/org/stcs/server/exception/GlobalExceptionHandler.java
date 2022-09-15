package org.stcs.server.exception;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.stcs.server.protocol.STCSProtocolBuilder;

/**
 * 全局异常信息处理
 *
 * @author Charles
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static JSONObject response(String code, String reason, String desc) {
        // 返回错误信息
        JSONObject response = STCSProtocolBuilder.buildFailure(code, reason, desc);
        try {
            log.error("occurring exception, response content:{}", response);
        } catch (Exception e) {
            log.error("Global exception build response message", e);
        }
        return response;
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public JSONObject handleOtherError(HttpServletRequest req, Exception e) {
        // 返回错误信息
        log.error("catch Exception,request url:{}", req.getRequestURL(), e);
        return response("UnknownException", "UnknownException", "unknown");
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public JSONObject handleRuntimeError(HttpServletRequest req, RuntimeException e) {
        log.error("catch RuntimeException,request url:{}", req.getRequestURL(), e);
        return response("RuntimeException", "RuntimeException", "unknown");
    }

    @ExceptionHandler(value = JSONException.class)
    @ResponseBody
    public JSONObject handleJSonProcessError(HttpServletRequest req, JSONException e) {
        log.error("catch JSONException, request url:{}", req.getRequestURL(), e);
        return response("JSONException", "JSONException", e.getMessage());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public JSONObject handleHttpMessageJSonProcessError(HttpServletRequest req, HttpMessageNotReadableException e) {
        log.error("catch HttpMessageNotReadableException, request url:{}", req.getRequestURL(), e);
        return response("HttpMessageNotReadableException", "HttpMessageNotReadableException", e.getMessage());
    }

    @ExceptionHandler(value = STCSException.class)
    @ResponseBody
    public JSONObject handleSTCSException(HttpServletRequest req, STCSException exp) {
        STCSExceptionEntity expEntity = exp.getSTCSExceptionEntity();
        String code = null;
        String reason = null;
        String description = null;
        if (expEntity != null) {
            code = expEntity.getCode();
            reason = expEntity.getReason();
            description = expEntity.getDescription();
        }
        log.error("STCSException error,url:{},dialogId:{},description:{}", req.getRequestURL(), exp.getSTCSExceptionEntity());
        return response(code, reason, description);
    }

}
