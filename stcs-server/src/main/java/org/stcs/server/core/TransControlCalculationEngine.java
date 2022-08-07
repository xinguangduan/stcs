package org.stcs.server.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.stcs.server.entity.TransPlanEntity;
import org.stcs.server.entity.TransStepEntity;
import org.stcs.server.utils.KeyUtils;

@Service
public class TransControlCalculationEngine {

    public List<TransStepEntity> calculate(TransPlanEntity transPlanEntity) {
        final List<TransStepEntity> steps = new ArrayList<>();
        // mock some data
        TransStepEntity step1 = new TransStepEntity();
        step1.setStepId(KeyUtils.generateUID());
        step1.setSequence(1);
        step1.setStepName("传送带1");
        steps.add(step1);

        TransStepEntity step2 = new TransStepEntity();
        step2.setStepId(KeyUtils.generateUID());
        step2.setSequence(2);
        step2.setStepName("传送带2");
        steps.add(step2);

        TransStepEntity step3 = new TransStepEntity();
        step3.setStepId(KeyUtils.generateUID());
        step3.setSequence(3);
        step3.setStepName("传送带3");
        steps.add(step3);

        return steps;
    }

}
