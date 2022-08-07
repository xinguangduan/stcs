package org.stcs.server.rest;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractRestController {
    public void receiveLog(JSONObject request) {
        if (log.isInfoEnabled()) {
            log.info("request content:{}", request);
        }
    }

    protected void costLog(final long begin, JSONObject res) {
        if (log.isInfoEnabled()) {
            final long cost = (System.currentTimeMillis() - begin);
            log.info("request process finished,cost(ms):{}," + ",response:{}", cost, res);
        }
    }

    protected void preCheck(JSONObject req) {
    }

}
