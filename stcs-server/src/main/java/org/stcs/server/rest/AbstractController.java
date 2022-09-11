package org.stcs.server.rest;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractController {
    public void receiveLog(JSONObject request) {
        if (log.isInfoEnabled()) {
            log.info("request content:{}", request);
        }
    }

    protected void preCheck(JSONObject req) {
        log.info("start pre check...");
    }
}
