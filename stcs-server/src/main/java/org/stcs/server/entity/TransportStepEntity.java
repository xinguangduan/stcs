package org.stcs.server.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TransportStepEntity {
    private String stepId;
    private int sequence;
    private String stepName;
}
