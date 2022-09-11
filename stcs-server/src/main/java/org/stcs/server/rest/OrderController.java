package org.stcs.server.rest;


import static org.stcs.server.constant.GlobalConstant.*;
import static org.stcs.server.protocol.STCSProtocolBuilder.*;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stcs.server.annotation.TakeTime;
import org.stcs.server.common.Pagination;
import org.stcs.server.entity.OrderEntity;
import org.stcs.server.exception.STCSException;
import org.stcs.server.service.OrderService;
import org.stcs.server.service.TransCalculationService;

@RestController
@Slf4j
@SecurityRequirement(name = "stcs")
@RequestMapping("/api/v1/orders")
public class OrderController extends AbstractController {

    private final OrderService orderService;
    private final TransCalculationService transCalculationService;

    @Autowired
    public OrderController(OrderService orderService, TransCalculationService transCalculationService) {
        this.orderService = orderService;
        this.transCalculationService = transCalculationService;
    }

    @TakeTime
    @GetMapping
    public ResponseEntity find() throws STCSException {
        JSONObject resp = null;
        final long beginTime = System.currentTimeMillis();
        receiveLog(null);
        preCheck(null);
        final List<OrderEntity> orderEntities = orderService.findAll();
        return ResponseEntity.ok().body(buildResponseCollections(orderEntities));
    }

    @TakeTime
    @GetMapping("/{orderId}")
    public ResponseEntity findOne(@PathVariable int orderId) throws STCSException {
        final OrderEntity orderEntity = orderService.find(orderId);
        return ResponseEntity.ok().body(buildResponseCollections(Arrays.asList(orderEntity)));
    }

    @TakeTime
    @GetMapping(value = "/{pageNum}/{pageSize}")
    public ResponseEntity find(@PathVariable int pageNum, @PathVariable int pageSize, @RequestBody(required = false) OrderEntity orderEntity) {
        Pagination page = Pagination.builder().pageNum(pageNum).pageSize(pageSize).build();
        final Pagination<OrderEntity> partEntities = orderService.find(page, orderEntity);
        return ResponseEntity.ok().body(buildResponsePagination(partEntities));
    }

    @TakeTime
    @PostMapping
    public ResponseEntity add(@RequestBody JSONObject req) {
        final OrderEntity orderEntity = JSON.to(OrderEntity.class, req);
        long count = orderService.add(orderEntity);
        JSONObject res = buildSuccess("add success");
        if (count <= 0) {
            res = buildFailure(ERROR_1001);
            return ResponseEntity.internalServerError().body(res);
        }
        return ResponseEntity.ok(res);
    }

    @TakeTime
    @PutMapping("/{orderId}")
    public ResponseEntity update(@RequestBody JSONObject req, @PathVariable int orderId) throws STCSException {
        final OrderEntity orderEntity = orderService.find(orderId);
        if (orderEntity == null) {
            return ResponseEntity.ok().body(buildFailure(ERROR_1005, "order not found"));
        }
        final OrderEntity newOrderEntity = JSON.to(OrderEntity.class, req);
        BeanUtils.copyProperties(newOrderEntity, orderEntity);
        long result = orderService.update(orderEntity);
        if (result > 0) {
            return ResponseEntity.ok(buildSuccess("update success"));
        }
        return ResponseEntity.ok().body(buildFailure(ERROR_1003, "update failure"));
    }

    @TakeTime
    @DeleteMapping("/{orderId}")
    public ResponseEntity delete(@PathVariable int orderId) {
        long result = orderService.delete(orderId);
        if (result > 0) {
            return ResponseEntity.ok(buildSuccess("delete success"));
        }
        return ResponseEntity.ok(buildFailure(ERROR_1002, "delete failure"));
    }

    @TakeTime
    @PostMapping("/{orderId}/start")
    public ResponseEntity start(@PathVariable int orderId) throws STCSException {
        log.info("start transportation ...");
        final OrderEntity orderEntity = orderService.find(orderId);
        if (orderEntity == null) {
            return ResponseEntity.ok().body(buildFailure("not found the order by " + orderId));
        }
        boolean res = transCalculationService.startTrans(orderEntity);
        return ResponseEntity.ok().body(buildSuccess("trans result " + res));
    }
}
