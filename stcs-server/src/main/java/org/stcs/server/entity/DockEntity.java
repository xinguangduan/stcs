package org.stcs.server.entity;

import lombok.*;

/**
 * 码头
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DockEntity {
    private int dockId;
    private String dockDesc;
}
