package org.stcs.server.entity;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransPlanEntity {
    private int planId;
    private int custId;
    private int orderId;
    private int dockId;
}
