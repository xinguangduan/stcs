package org.stcs.server.entity;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransPathEntity {
    private int pathId;
    private int partId;
    private int orderId;
    private String pathDef;
    private String currentNode;
}
