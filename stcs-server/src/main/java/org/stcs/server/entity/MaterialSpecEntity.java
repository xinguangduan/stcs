package org.stcs.server.entity;

import lombok.*;

/**
 * 物料规格
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MaterialSpecEntity {
    private int materialId;
    private String materialDesc;
    private String materialSpec;
}
