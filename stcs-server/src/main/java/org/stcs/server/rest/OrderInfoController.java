package org.stcs.server.rest;


import static org.stcs.server.constant.GlobalConstant.ERROR_CODE_1000;
import static org.stcs.server.constant.GlobalConstant.SUCCESS_CODE;

import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.stcs.server.entity.OrderInfoEntity;
import org.stcs.server.entity.STCSExceptionEntity;
import org.stcs.server.exception.STCSException;
import org.stcs.server.protocol.STCSProtocolBuilder;
import org.stcs.server.service.OrderInfoService;
import org.stcs.server.service.TransCalculationService;
import org.stcs.server.utils.KeyUtils;

@RestController
@Slf4j
@RequestMapping(value = "/stcs/v1/order/")
public class OrderInfoController extends AbstractRestController {

    private final OrderInfoService orderService;
    private final TransCalculationService transCalculationService;

    @Autowired
    public OrderInfoController(OrderInfoService orderService, TransCalculationService transCalculationService) {
        this.orderService = orderService;
        this.transCalculationService = transCalculationService;
    }

    /**
     * get all orders
     *
     * @return
     */
    @GetMapping(value = "/find")
    public JSONObject findOrder() throws STCSException {
        JSONObject resp = null;
        final long beginTime = System.currentTimeMillis();
        receiveLog(null);
        preCheck(null);
        try {
            List<OrderInfoEntity> orderInfoEntities = orderService.findAll();
            JSONArray orders = JSONArray.of(orderInfoEntities.toArray());
            resp = new JSONObject();
            resp.put("orders", orders);
            resp.put("total", orders.size());
            resp.put("code", SUCCESS_CODE);
            return resp;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            STCSExceptionEntity entity = STCSExceptionEntity.builder()
                    .code(ERROR_CODE_1000)
                    .reason("some thing fa")
                    .description(e.getMessage())
                    .messageId(KeyUtils.generateMessageId())
                    .build();
            throw new STCSException(entity);
        } finally {
            costLog(beginTime, resp);
        }
    }

    @PostMapping(value = "/add")
    public JSONObject addOrder(@RequestBody JSONObject req) {
        OrderInfoEntity orderInfoEntity = JSON.to(OrderInfoEntity.class, req);
        orderService.add(orderInfoEntity);
        return STCSProtocolBuilder.buildSuccess(KeyUtils.generateMessageId(), SUCCESS_CODE);
    }

    @PostMapping(value = "/update")
    public JSONObject update(@RequestBody JSONObject req) {
        OrderInfoEntity orderInfoEntity = JSON.to(OrderInfoEntity.class, req);
        orderService.update(orderInfoEntity);
        return STCSProtocolBuilder.buildSuccess("messageId", "");
    }

    @PostMapping(value = "/start")
    public JSONObject start(@RequestBody JSONObject req) {
        int orderId = req.getIntValue("orderId");
        OrderInfoEntity orderInfoEntity = orderService.find(orderId);
        if (orderInfoEntity == null) {
            return STCSProtocolBuilder.buildFailure(KeyUtils.generateMessageId(), "not found the order by " + orderId);
        }
        boolean res = transCalculationService.startTrans(orderInfoEntity);
        return STCSProtocolBuilder.buildSuccess(KeyUtils.generateMessageId(), "trans result " + res);
    }


}
