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
import org.stcs.server.entity.MaterialSpecEntity;
import org.stcs.server.service.MaterialSpecService;

@RestController
@Slf4j
@RequestMapping(value = "api/v1/materials")
public class MaterialSpecController extends AbstractRestController {

    private final MaterialSpecService materialSpecService;

    @Autowired
    public MaterialSpecController(MaterialSpecService materialSpecService) {
        this.materialSpecService = materialSpecService;
    }

    @GetMapping
    public ResponseEntity find() {
        final List<MaterialSpecEntity> materialSpecEntities = materialSpecService.findAll();
        log.info("find result {}", materialSpecEntities);
        JSONObject resp = buildResponseCollections(materialSpecEntities);
        return ResponseEntity.ok().body(resp);
    }

    @GetMapping(value = "/{materialId}")
    public ResponseEntity find(@PathVariable int materialId) {
        final MaterialSpecEntity materialSpecEntity = materialSpecService.find(materialId);
        log.info("find result {}", materialSpecEntity);
        JSONObject resp = buildResponseCollections(Arrays.asList(materialSpecEntity));
        return ResponseEntity.ok().body(resp);
    }

    @PostMapping
    public ResponseEntity add(@RequestBody String req) {
        List<MaterialSpecEntity> materialSpecEntities = JSON.parseArray(req, MaterialSpecEntity.class);
        long result = materialSpecService.add(materialSpecEntities);
        if (result > 0) {
            return ResponseEntity.ok().body(buildSuccess());
        }
        return ResponseEntity.ok(buildFailure(ERROR_1001, "add failure"));
    }

    @PutMapping(value = "/{materialId}")
    public ResponseEntity update(@RequestBody String req, @PathVariable int materialId) {
        final MaterialSpecEntity oldMaterialSpecEntity = materialSpecService.find(materialId);
        if (oldMaterialSpecEntity == null) {
            return ResponseEntity.ok().body(buildFailure(ERROR_1005, "not found the material spec"));
        }
        final MaterialSpecEntity materialSpecEntity = JSON.to(MaterialSpecEntity.class, req);
        long result = materialSpecService.update(materialSpecEntity);
        if (result > 0) {
            return ResponseEntity.ok(buildSuccess("update success"));
        }
        return ResponseEntity.ok(buildFailure(ERROR_1003, "update failure"));
    }

    @DeleteMapping(value = "/{materialId}")
    public ResponseEntity delete(@PathVariable int materialId) {
        long result = materialSpecService.delete(materialId);
        if (result > 0) {
            return ResponseEntity.ok(buildSuccess("delete success"));
        }
        return ResponseEntity.ok(buildFailure(ERROR_1002, "update failure"));
    }
}
