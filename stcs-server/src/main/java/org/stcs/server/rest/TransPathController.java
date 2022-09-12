package org.stcs.server.rest;

import static org.stcs.server.constant.GlobalConstant.*;
import static org.stcs.server.protocol.STCSProtocolBuilder.*;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stcs.server.annotation.LatencyTime;
import org.stcs.server.common.Pagination;
import org.stcs.server.entity.TransPathEntity;
import org.stcs.server.exception.STCSException;
import org.stcs.server.service.TransCalculationService;
import org.stcs.server.service.TransPathService;

@RestController
@Slf4j
@RequestMapping("/api/v1/transpaths")
public class TransPathController extends AbstractController {

    private final TransPathService pathService;
    private final TransCalculationService transCalculationService;

    @Autowired
    public TransPathController(TransPathService pathService, TransCalculationService transCalculationService) {
        this.pathService = pathService;
        this.transCalculationService = transCalculationService;
    }

    @LatencyTime
    @GetMapping
    public ResponseEntity find() {
        final List<TransPathEntity> pathEntities = pathService.findAll();
        return ResponseEntity.ok().body(buildResponseCollections(pathEntities));
    }

    @LatencyTime
    @GetMapping(value = "/{pathId}")
    public ResponseEntity findOne(@PathVariable int pathId) throws STCSException {
        final TransPathEntity planEntity = pathService.find(pathId);
        return ResponseEntity.ok().body(buildResponseCollections(Arrays.asList(planEntity)));
    }

    @LatencyTime
    @GetMapping(value = "/{pageNum}/{pageSize}")
    public ResponseEntity find(@PathVariable int pageNum, @PathVariable int pageSize, @RequestBody(required = false) TransPathEntity pathEntity) {
        Pagination page = Pagination.builder().pageNum(pageNum).pageSize(pageSize).build();
        final Pagination<TransPathEntity> entityPagination = pathService.find(page, pathEntity);
        return ResponseEntity.ok().body(buildResponsePagination(entityPagination));
    }

    @LatencyTime
    @PostMapping
    public ResponseEntity add(@RequestBody String req) {
        final List<TransPathEntity> pathEntities = JSON.parseArray(req, TransPathEntity.class);
        long result = pathService.add(pathEntities);
        JSONObject res = buildSuccess("add success");
        if (result <= 0) {
            res = buildFailure(ERROR_1001);
            return ResponseEntity.internalServerError().body(res);
        }
        return ResponseEntity.ok(res);
    }

    @LatencyTime
    @PatchMapping("/{pathId}")
    public ResponseEntity transport(@RequestBody String req, @PathVariable int pathId) throws STCSException {
        final TransPathEntity pathEntity = JSON.parseObject(req, TransPathEntity.class);
        final JSONObject resp = transCalculationService.transport(pathEntity);
        return ResponseEntity.ok(resp);
    }

    @LatencyTime
    @PutMapping(value = "/{pathId}")
    public ResponseEntity update(@RequestBody String req, @PathVariable int pathId) throws STCSException {
        final TransPathEntity pathEntity = pathService.find(pathId);
        if (pathEntity == null) {
            return ResponseEntity.ok().body(buildFailure(ERROR_1005, "order not found"));
        }
        final TransPathEntity newPlanEntity = JSON.to(TransPathEntity.class, req);
        long result = pathService.update(newPlanEntity);
        JSONObject resp = buildSuccess("update success");
        if (result <= 0) {
            resp = buildFailure(ERROR_1003, "update failure");
        }
        return ResponseEntity.ok().body(resp);
    }

    @LatencyTime
    @DeleteMapping(value = "/{pathId}")
    public ResponseEntity delete(@PathVariable int pathId) {
        long result = pathService.delete(pathId);
        JSONObject resp = buildSuccess("delete success");
        if (result <= 0) {
            resp = buildFailure(ERROR_1002, "delete failure");
        }
        return ResponseEntity.ok().body(resp);
    }

}
