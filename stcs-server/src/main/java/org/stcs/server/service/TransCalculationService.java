package org.stcs.server.service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.stcs.server.core.TransControlCalculationEngine;
import org.stcs.server.entity.OrderInfoEntity;
import org.stcs.server.entity.TransPlanEntity;
import org.stcs.server.entity.TransStepEntity;

@Service
@Slf4j
public class TransCalculationService {

    private final TransControlCalculationEngine transControlCalculationEngine;

    public TransCalculationService(TransControlCalculationEngine transControlCalculationEngine) {
        this.transControlCalculationEngine = transControlCalculationEngine;
    }

    public boolean startTrans(OrderInfoEntity orderInfoEntity) {

        log.info("start execute transport steps...");
        TransPlanEntity transPlanEntity = new TransPlanEntity();

        List<TransStepEntity> steps = transControlCalculationEngine.calculate(transPlanEntity);

        for (TransStepEntity step : steps) {
            log.info("current execute step :{}", step);
        }
        log.info("All steps are complete.");
        return true;
    }
}
