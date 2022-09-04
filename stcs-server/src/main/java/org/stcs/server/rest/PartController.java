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
import org.stcs.server.entity.PartEntity;
import org.stcs.server.service.PartService;

@RestController
@Slf4j
@RequestMapping(value = "api/v1/parts")
public class PartController extends AbstractRestController {

    private final PartService partService;

    @Autowired
    public PartController(PartService partService) {
        this.partService = partService;
    }

    @GetMapping
    public ResponseEntity find() {
        final List<PartEntity> partEntities = partService.findAll();
        log.info("find result {}", partEntities);
        return ResponseEntity.ok().body(buildResponseCollections(partEntities));
    }

    @GetMapping(value = "/{partId}")
    public ResponseEntity findOne(@PathVariable int partId) {
        final PartEntity partEntity = partService.find(partId);
        log.info("find result {}", partEntity);
        return ResponseEntity.ok().body(buildResponseCollections(Arrays.asList(partEntity)));
    }

    @PostMapping
    public ResponseEntity add(@RequestBody String req) {
        final List<PartEntity> partEntities = JSON.parseArray(req, PartEntity.class);
        long result = partService.add(partEntities);
        JSONObject res = buildSuccess("add success");
        if (result <= 0) {
            res = buildFailure(ERROR_1001);
            return ResponseEntity.internalServerError().body(res);
        }
        return ResponseEntity.ok(res);
    }

    @PutMapping(value = "/{partId}")
    public ResponseEntity update(@RequestBody String req, @PathVariable int partId) {
        final PartEntity partEntity = partService.find(partId);
        if (partEntity == null) {
            return ResponseEntity.ok().body(buildFailure(ERROR_1005, "order not found"));
        }
        final PartEntity newPartEntity = JSON.to(PartEntity.class, req);
        BeanUtils.copyProperties(newPartEntity, partEntity);
        long result = partService.update(partEntity);
        JSONObject resp = buildSuccess("update success");
        if (result <= 0) {
            resp = buildFailure(ERROR_1003, "update failure");
        }
        return ResponseEntity.ok().body(resp);
    }

    @DeleteMapping(value = "/{partId}")
    public ResponseEntity delete(@PathVariable int partId) {
        long result = partService.delete(partId);
        JSONObject resp = buildSuccess("delete success");
        if (result <= 0) {
            resp = buildFailure(ERROR_1002, "delete failure");
        }
        return ResponseEntity.ok().body(resp);
    }

}
