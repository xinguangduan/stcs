package org.stcs.server.rest;

import static org.stcs.server.constant.GlobalConstant.*;
import static org.stcs.server.protocol.STCSProtocolBuilder.*;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stcs.server.annotation.TakeTime;
import org.stcs.server.common.Pagination;
import org.stcs.server.entity.TransportPlanEntity;
import org.stcs.server.exception.STCSException;
import org.stcs.server.service.TransportPlanService;

@RestController
@Slf4j
@RequestMapping("/api/v1/transplans")
public class PlanController extends AbstractController {

    private final TransportPlanService planService;

    @Autowired
    public PlanController(TransportPlanService planService) {
        this.planService = planService;
    }

    @TakeTime
    @GetMapping
    public ResponseEntity find() {
        final List<TransportPlanEntity> planEntities = planService.findAll();
        return ResponseEntity.ok().body(buildResponseCollections(planEntities));
    }

    @TakeTime
    @GetMapping(value = "/{planId}")
    public ResponseEntity findOne(@PathVariable int planId) throws STCSException {
        final TransportPlanEntity planEntity = planService.find(planId);
        return ResponseEntity.ok().body(buildResponseCollections(Arrays.asList(planEntity)));
    }

    @TakeTime
    @GetMapping(value = "/{pageNum}/{pageSize}")
    public ResponseEntity find(@PathVariable int pageNum, @PathVariable int pageSize, @RequestBody(required = false) TransportPlanEntity part) {
        Pagination page = Pagination.builder().pageNum(pageNum).pageSize(pageSize).build();
        final Pagination<TransportPlanEntity> partEntities = planService.find(page, part);
        return ResponseEntity.ok().body(buildResponsePagination(partEntities));
    }

    @TakeTime
    @PostMapping
    public ResponseEntity add(@RequestBody String req) {
        final List<TransportPlanEntity> planEntities = JSON.parseArray(req, TransportPlanEntity.class);
        long result = planService.add(planEntities);
        JSONObject res = buildSuccess("add success");
        if (result <= 0) {
            res = buildFailure(ERROR_1001);
            return ResponseEntity.internalServerError().body(res);
        }
        return ResponseEntity.ok(res);
    }

    @TakeTime
    @PutMapping(value = "/{planId}")
    public ResponseEntity update(@RequestBody String req, @PathVariable int planId) throws STCSException {
        final TransportPlanEntity planEntity = planService.find(planId);
        if (planEntity == null) {
            return ResponseEntity.ok().body(buildFailure(ERROR_1005, "order not found"));
        }
        final TransportPlanEntity newPlanEntity = JSON.to(TransportPlanEntity.class, req);
        BeanUtils.copyProperties(newPlanEntity, planEntity);
        long result = planService.update(planEntity);
        JSONObject resp = buildSuccess("update success");
        if (result <= 0) {
            resp = buildFailure(ERROR_1003, "update failure");
        }
        return ResponseEntity.ok().body(resp);
    }

    @TakeTime
    @DeleteMapping(value = "/{planId}")
    public ResponseEntity delete(@PathVariable int planId) {
        long result = planService.delete(planId);
        JSONObject resp = buildSuccess("delete success");
        if (result <= 0) {
            resp = buildFailure(ERROR_1002, "delete failure");
        }
        return ResponseEntity.ok().body(resp);
    }

}
