package org.stcs.server.rest;

import static org.stcs.server.constant.GlobalConstant.*;
import static org.stcs.server.protocol.STCSProtocolBuilder.*;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stcs.server.annotation.TakeTime;
import org.stcs.server.common.Pagination;
import org.stcs.server.entity.MaterialSpecEntity;
import org.stcs.server.exception.STCSException;
import org.stcs.server.service.MaterialSpecService;

@RestController
@Slf4j
@SecurityRequirement(name = "stcs")
@RequestMapping("/api/v1/materials")
public class MaterialSpecController extends AbstractController {

    private final MaterialSpecService materialSpecService;

    @Autowired
    public MaterialSpecController(MaterialSpecService materialSpecService) {
        this.materialSpecService = materialSpecService;
    }

    @TakeTime
    @GetMapping
    public ResponseEntity find() {
        final List<MaterialSpecEntity> materialSpecEntities = materialSpecService.findAll();
        JSONObject resp = buildResponseCollections(materialSpecEntities);
        return ResponseEntity.ok().body(resp);
    }

    @TakeTime
    @GetMapping("/{materialId}")
    public ResponseEntity find(@PathVariable int materialId) throws STCSException {
        final MaterialSpecEntity materialSpecEntity = materialSpecService.find(materialId);
        JSONObject resp = buildResponseCollections(Arrays.asList(materialSpecEntity));
        return ResponseEntity.ok().body(resp);
    }

    @TakeTime
    @GetMapping(value = "/{pageNum}/{pageSize}")
    public ResponseEntity find(@PathVariable int pageNum, @PathVariable int pageSize, @RequestBody(required = false) MaterialSpecEntity materialSpecEntity) {
        Pagination page = Pagination.builder().pageNum(pageNum).pageSize(pageSize).build();
        final Pagination<MaterialSpecEntity> partEntities = materialSpecService.find(page, materialSpecEntity);
        return ResponseEntity.ok().body(buildResponsePagination(partEntities));
    }

    @TakeTime
    @PostMapping
    public ResponseEntity add(@RequestBody String req) {
        List<MaterialSpecEntity> materialSpecEntities = JSON.parseArray(req, MaterialSpecEntity.class);
        long result = materialSpecService.add(materialSpecEntities);
        if (result > 0) {
            return ResponseEntity.ok().body(buildSuccess());
        }
        return ResponseEntity.ok(buildFailure(ERROR_1001, "add failure"));
    }

    @TakeTime
    @PutMapping("/{materialId}")
    public ResponseEntity update(@RequestBody String req, @PathVariable int materialId) throws STCSException {
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

    @TakeTime
    @DeleteMapping("/{materialId}")
    public ResponseEntity delete(@PathVariable int materialId) throws STCSException {
        long result = materialSpecService.delete(materialId);
        if (result > 0) {
            return ResponseEntity.ok(buildSuccess("delete success"));
        }
        return ResponseEntity.ok(buildFailure(ERROR_1002, "update failure"));
    }
}
