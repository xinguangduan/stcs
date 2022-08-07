package org.stcs.server.entity;

import lombok.Data;

@Data
public class TransPlanEntity {
    private String planId;
    private String custId;
    private String orderId;
    private String dockId;
}
