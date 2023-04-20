package org.stcs.server.rest;


import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.stcs.server.annotation.JSONValidation;
import org.stcs.server.annotation.LatencyTime;

@RestController
@Slf4j
public class ValidatorController {

    @LatencyTime
    @PostMapping("/test1")
    @JSONValidation(schemaName = "s.json")
    public Map<String, Object> test(@RequestBody Map<String, Object> map) {

        log.info("接收的数据：" + map.toString());

        Map<String, Object> responseMap = new HashMap<>();

        responseMap.put("ResponseStatusListObject", "success");

        return responseMap;

    }

}
