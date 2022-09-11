package org.stcs.server.service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.stcs.server.core.TransControlCalculationEngine;
import org.stcs.server.entity.OrderEntity;
import org.stcs.server.entity.TransportPlanEntity;
import org.stcs.server.entity.TransportStepEntity;

@Service
@Slf4j
public class TransCalculationService extends AbstractService {

    private final TransControlCalculationEngine transControlCalculationEngine;

    public TransCalculationService(TransControlCalculationEngine transControlCalculationEngine) {
        this.transControlCalculationEngine = transControlCalculationEngine;
    }

    public boolean startTrans(OrderEntity orderEntity) {

        log.info("start execute transport steps...");
        TransportPlanEntity transportPlanEntity = new TransportPlanEntity();

        List<TransportStepEntity> steps = transControlCalculationEngine.calculate(transportPlanEntity);

        for (TransportStepEntity step : steps) {
            log.info("current execute step :{}", step);
        }
        log.info("All steps are complete.");
        return true;
    }
}
