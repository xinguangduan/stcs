package org.stcs.server.entity;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NodeGraphEntity {
    private String nodeFrom;
    private String nodeTo;
    private int distance;
}
