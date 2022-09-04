package org.stcs.server.exception;

import java.io.Serializable;

import com.alibaba.fastjson2.JSONObject;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class STCSExceptionEntity implements Serializable {
    private static final long serialVersionUID = 1905122041950251507L;
    private String code;
    private String reason;
    private String description;
    private String messageId;
    private JSONObject requestMessage;
}
