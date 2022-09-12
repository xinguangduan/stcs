package org.stcs.server.protocol;


import static org.stcs.server.constant.GlobalConstant.SUCCESS_CODE;

import java.util.Collection;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.stcs.server.common.Pagination;
import org.stcs.server.constant.ResultType;
import org.stcs.server.utils.KeyUtils;

public final class STCSProtocolBuilder {

    private STCSProtocolBuilder() {
    }

    public static JSONObject buildSuccess() {
        final JSONObject content = new JSONObject();
        content.put("code", SUCCESS_CODE);
        content.put("messageId", KeyUtils.generateMessageId());
        return content;
    }

    public static JSONObject buildSuccess(String message) {
        final JSONObject content = new JSONObject();
        content.put("code", SUCCESS_CODE);
        content.put("message", message);
        content.put("messageId", KeyUtils.generateMessageId());
        return content;
    }

    public static JSONObject buildSuccess(String message, String messageId) {
        final JSONObject content = new JSONObject();
        content.put("code", SUCCESS_CODE);
        content.put("message", message);
        content.put("messageId", messageId);
        return content;
    }

    public static JSONObject buildFailure(String messageId, String code, String reason) {
        final JSONObject content = new JSONObject();
        content.put("code", code);
        content.put("reason", reason);
        content.put("messageId", messageId);
        return content;
    }

    public static JSONObject buildFailure(String code) {
        final JSONObject content = new JSONObject();
        content.put("code", code);
        content.put("reason", code);
        content.put("messageId", KeyUtils.generateMessageId());
        return content;
    }

    public static JSONObject buildFailure(String code, String reason) {
        final JSONObject content = new JSONObject();
        content.put("code", code);
        content.put("reason", reason);
        content.put("messageId", KeyUtils.generateMessageId());
        return content;
    }

    public static JSONObject buildFailure(ResultType resultType) {
        final JSONObject content = new JSONObject();
        content.put("code", resultType.getCode());
        content.put("reason", resultType.getInfo());
        content.put("messageId", KeyUtils.generateMessageId());
        return content;
    }

    public static <T> JSONObject buildResponseCollections(Collection<T> t) {
        final JSONObject resp = new JSONObject();
        resp.put("code", SUCCESS_CODE);
        resp.put("total", t.size());
        resp.put("records", t);
        return resp;
    }

    public static <T> JSONObject buildResponsePagination(Pagination<T> p) {
        final JSONObject resp = (JSONObject) JSON.toJSON(p);
        resp.put("code", SUCCESS_CODE);
//        resp.put("total", p.getTotal());
//        resp.put("total", p.);
//        resp.put("records", p.getRecords());
        return resp;
    }

    public static JSONObject buildTransResponse(String message, String currentNode, String nextNode, int distance, boolean isEnd) {
        final JSONObject content = new JSONObject();
        content.put("code", SUCCESS_CODE);
        content.put("message", message);
        content.put("messageId", KeyUtils.generateMessageId());
        JSONObject data = new JSONObject();
        JSONObject transPath = new JSONObject();
        data.put("transPath", transPath);

        transPath.put("currentNode", currentNode);
        transPath.put("nextNode", nextNode);
        transPath.put("distance", distance);
        transPath.put("end", isEnd);
        content.put("data", data);
        return content;
    }

}
