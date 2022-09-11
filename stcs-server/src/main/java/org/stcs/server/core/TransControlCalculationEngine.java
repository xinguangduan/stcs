package org.stcs.server.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.stcs.server.entity.TransportPlanEntity;
import org.stcs.server.entity.TransportStepEntity;
import org.stcs.server.utils.KeyUtils;

@Service
public class TransControlCalculationEngine {

    public List<TransportStepEntity> calculate(TransportPlanEntity transportPlanEntity) {
        final List<TransportStepEntity> steps = new ArrayList<>();
        // mock some data
        TransportStepEntity step1 = new TransportStepEntity();
        step1.setStepId(KeyUtils.generateUID());
        step1.setSequence(1);
        step1.setStepName("传送带1");
        steps.add(step1);

        TransportStepEntity step2 = new TransportStepEntity();
        step2.setStepId(KeyUtils.generateUID());
        step2.setSequence(2);
        step2.setStepName("传送带2");
        steps.add(step2);

        TransportStepEntity step3 = new TransportStepEntity();
        step3.setStepId(KeyUtils.generateUID());
        step3.setSequence(3);
        step3.setStepName("传送带3");
        steps.add(step3);

        return steps;
    }

}
