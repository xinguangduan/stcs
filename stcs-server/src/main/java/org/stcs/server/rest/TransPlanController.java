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
import org.stcs.server.annotation.LatencyTime;
import org.stcs.server.common.Pagination;
import org.stcs.server.entity.TransPlanEntity;
import org.stcs.server.exception.STCSException;
import org.stcs.server.service.TransPlanService;

@RestController
@Slf4j
@SecurityRequirement(name = "stcs")
@RequestMapping("/api/v1/transplans")
public class TransPlanController extends AbstractController {

    private final TransPlanService planService;

    @Autowired
    public TransPlanController(TransPlanService planService) {
        this.planService = planService;
    }

    @LatencyTime
    @GetMapping
    public ResponseEntity find() {
        final List<TransPlanEntity> planEntities = planService.findAll();
        return ResponseEntity.ok().body(buildResponseCollections(planEntities));
    }

    @LatencyTime
    @GetMapping(value = "/{planId}")
    public ResponseEntity findOne(@PathVariable int planId) throws STCSException {
        final TransPlanEntity planEntity = planService.find(planId);
        return ResponseEntity.ok().body(buildResponseCollections(Arrays.asList(planEntity)));
    }

    @LatencyTime
    @GetMapping(value = "/{pageNum}/{pageSize}")
    public ResponseEntity find(@PathVariable int pageNum, @PathVariable int pageSize, @RequestBody(required = false) TransPlanEntity part) {
        Pagination page = Pagination.builder().pageNum(pageNum).pageSize(pageSize).build();
        final Pagination<TransPlanEntity> partEntities = planService.find(page, part);
        return ResponseEntity.ok().body(buildResponsePagination(partEntities));
    }

    @LatencyTime
    @PostMapping
    public ResponseEntity add(@RequestBody String req) {
        final List<TransPlanEntity> planEntities = JSON.parseArray(req, TransPlanEntity.class);
        long result = planService.add(planEntities);
        JSONObject res = buildSuccess("add success");
        if (result <= 0) {
            res = buildFailure(ERROR_1001);
            return ResponseEntity.internalServerError().body(res);
        }
        return ResponseEntity.ok(res);
    }

    @LatencyTime
    @PutMapping(value = "/{planId}")
    public ResponseEntity update(@RequestBody String req, @PathVariable int planId) throws STCSException {
        final TransPlanEntity planEntity = planService.find(planId);
        if (planEntity == null) {
            return ResponseEntity.ok().body(buildFailure(ERROR_1005, "order not found"));
        }
        final TransPlanEntity newPlanEntity = JSON.to(TransPlanEntity.class, req);
        BeanUtils.copyProperties(newPlanEntity, planEntity);
        long result = planService.update(planEntity);
        JSONObject resp = buildSuccess("update success");
        if (result <= 0) {
            resp = buildFailure(ERROR_1003, "update failure");
        }
        return ResponseEntity.ok().body(resp);
    }

    @LatencyTime
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
