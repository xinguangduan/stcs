package org.stcs.server.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TransStepEntity {
    private String stepId;
    private int sequence;
    private String stepName;
}
