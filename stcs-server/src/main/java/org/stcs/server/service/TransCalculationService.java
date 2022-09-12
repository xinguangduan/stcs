package org.stcs.server.service;

import static org.stcs.server.protocol.STCSProtocolBuilder.buildTransResponse;

import java.util.List;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.stcs.server.core.TransControlCalculationEngine;
import org.stcs.server.entity.*;
import org.stcs.server.exception.STCSException;

@Service
@Slf4j
public class TransCalculationService extends AbstractService {

    private final TransControlCalculationEngine transControlCalculationEngine;
    private final TransPathService pathService;
    private final NodeGraphService nodeGraphService;

    public TransCalculationService(TransControlCalculationEngine transControlCalculationEngine, TransPathService pathService, NodeGraphService nodeGraphService) {
        this.transControlCalculationEngine = transControlCalculationEngine;
        this.pathService = pathService;
        this.nodeGraphService = nodeGraphService;
    }

    public boolean startTrans(OrderEntity orderEntity) {

        log.info("start execute transport steps...");
        TransPlanEntity transPlanEntity = new TransPlanEntity();

        List<TransStepEntity> steps = transControlCalculationEngine.calculate(transPlanEntity);

        for (TransStepEntity step : steps) {
            log.info("current execute step :{}", step);
        }
        log.info("All steps are complete.");
        return true;
    }

    public JSONObject transport(TransPathEntity pathEntity) throws STCSException {
        TransPathEntity transPathEntity = pathService.find(pathEntity.getPathId());
        final String pathDef = transPathEntity.getPathDef();
        final String currentNode = pathEntity.getCurrentNode();

        if (pathDef.endsWith(currentNode)) {
            JSONObject resp = buildTransResponse("transport ok", currentNode, "", 0, true);
            return resp;
        }

        String[] nodes = pathDef.split(",");
        int nextNodeIndex = 0;
        int totalNodes = nodes.length;
        boolean isEnd = false;
        for (int i = 0; i < totalNodes; i++) {
            final String node = nodes[i];
            if (node.equalsIgnoreCase(currentNode)) {
                if ((i + 1) < totalNodes) {
                    nextNodeIndex = i + 1;
                } else {
                    nextNodeIndex = i;
                    isEnd = true;
                }
                break;
            }
        }
        String nextNode = nodes[nextNodeIndex];
        final NodeGraphEntity nodeGraphEntity = nodeGraphService.find(currentNode, nextNode);
        int distance = 0;
        if (nodeGraphEntity != null) {
            distance = nodeGraphEntity.getDistance();
        }
        JSONObject resp = buildTransResponse("transport ok", currentNode, nextNode, distance, isEnd);
        return resp;
    }
}
