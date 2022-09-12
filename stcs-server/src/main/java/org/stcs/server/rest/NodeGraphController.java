package org.stcs.server.rest;

import static org.stcs.server.constant.GlobalConstant.ERROR_1001;
import static org.stcs.server.constant.GlobalConstant.ERROR_1002;
import static org.stcs.server.protocol.STCSProtocolBuilder.*;

import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stcs.server.annotation.LatencyTime;
import org.stcs.server.entity.NodeGraphEntity;
import org.stcs.server.service.NodeGraphService;

@RestController
@Slf4j
@RequestMapping("/api/v1/nodegraphs")
public class NodeGraphController extends AbstractController {

    private final NodeGraphService nodeGraphService;

    @Autowired
    public NodeGraphController(NodeGraphService nodeGraphService) {
        this.nodeGraphService = nodeGraphService;
    }

    @LatencyTime
    @GetMapping
    public ResponseEntity find() {
        final List<NodeGraphEntity> nodeGraphEntities = nodeGraphService.findAll();
        return ResponseEntity.ok().body(buildResponseCollections(nodeGraphEntities));
    }

    @LatencyTime
    @PostMapping
    public ResponseEntity add(@RequestBody String req) {
        final List<NodeGraphEntity> pathEntities = JSON.parseArray(req, NodeGraphEntity.class);
        long result = nodeGraphService.add(pathEntities);
        JSONObject res = buildSuccess("add success");
        if (result <= 0) {
            res = buildFailure(ERROR_1001);
            return ResponseEntity.internalServerError().body(res);
        }
        return ResponseEntity.ok(res);
    }

    @LatencyTime
    @DeleteMapping
    public ResponseEntity delete() {
        long result = nodeGraphService.delete();
        JSONObject resp = buildSuccess("delete success");
        if (result <= 0) {
            resp = buildFailure(ERROR_1002, "delete failure");
        }
        return ResponseEntity.ok().body(resp);
    }
}
