package org.stcs.server.protocol;


import com.alibaba.fastjson2.JSONObject;

public final class STCSProtocolBuilder {

    private STCSProtocolBuilder() {
    }

    public static JSONObject buildSuccess(String messageId, String message) {
        JSONObject content = new JSONObject();
        content.put("message", message);
        content.put("messageId", messageId);
        return content;
    }

    public static JSONObject buildFailure(String messageId, String message) {
        JSONObject content = new JSONObject();
        content.put("message", message);
        content.put("messageId", messageId);
        return content;
    }

}
