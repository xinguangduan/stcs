package org.stcs.server.rest;

import static org.stcs.server.constant.GlobalConstant.*;
import static org.stcs.server.protocol.STCSProtocolBuilder.*;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson2.JSON;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stcs.server.annotation.TakeTime;
import org.stcs.server.common.Pagination;
import org.stcs.server.entity.DockEntity;
import org.stcs.server.exception.STCSException;
import org.stcs.server.service.DockService;

@RestController
@Slf4j
@SecurityRequirement(name = "stcs")
@RequestMapping("/api/v1/docks")
public class DockController extends AbstractController {

    private final DockService dockService;

    @Autowired
    public DockController(DockService dockService) {
        this.dockService = dockService;
    }

    @TakeTime
    @GetMapping
    public ResponseEntity find() {
        final List<DockEntity> dockEntities = dockService.findAll();
        log.info("find result {}", dockEntities);
        return ResponseEntity.ok().body(buildResponseCollections(dockEntities));
    }

    @TakeTime
    @GetMapping("/{dockId}")
    public ResponseEntity findOne(@PathVariable int dockId) throws STCSException {
        final DockEntity dockEntity = dockService.find(dockId);
        if (dockEntity == null) {
            return ResponseEntity.ok().body(buildFailure(ERROR_1005, "dock not found"));
        }
        return ResponseEntity.ok().body(buildResponseCollections(Arrays.asList(dockEntity)));
    }

    @TakeTime
    @GetMapping(value = "/{pageNum}/{pageSize}")
    public ResponseEntity find(@PathVariable int pageNum, @PathVariable int pageSize, @RequestBody(required = false) DockEntity dockEntity) {
        Pagination page = Pagination.builder().pageNum(pageNum).pageSize(pageSize).build();
        final Pagination<DockEntity> partEntities = dockService.find(page, dockEntity);
        return ResponseEntity.ok().body(buildResponsePagination(partEntities));
    }

    @TakeTime
    @PostMapping
    public ResponseEntity add(@RequestBody String req) {
        final List<DockEntity> dockEntities = JSON.parseArray(req, DockEntity.class);
        long result = dockService.add(dockEntities);
        if (result > 0) {
            return ResponseEntity.ok().body(buildSuccess());
        }
        return ResponseEntity.ok(buildFailure(ERROR_1001, "add failure"));
    }

    @TakeTime
    @PutMapping("/{dockId}")
    public ResponseEntity update(@RequestBody String req, @PathVariable int dockId) throws STCSException {
        final DockEntity dockEntity = dockService.find(dockId);
        if (dockEntity == null) {
            return ResponseEntity.ok().body(buildFailure(ERROR_1005, "dock not found"));
        }
        final DockEntity newDockEntity = JSON.to(DockEntity.class, req);
        long res = dockService.update(newDockEntity);
        if (res > 0) {
            return ResponseEntity.ok().body(buildSuccess("update success."));
        }
        return ResponseEntity.ok().body(buildFailure(ERROR_1003, "update failure"));
    }

    @TakeTime
    @DeleteMapping("/{dockId}")
    public ResponseEntity delete(@PathVariable int dockId) {
        long result = dockService.delete(dockId);
        log.info("delete count {}, dockId {}", result, dockId);
        if (result > 0) {
            return ResponseEntity.ok().body(buildSuccess("delete success."));
        }
        return ResponseEntity.ok().body(buildFailure(ERROR_1002, "delete failure"));
    }
}
