package org.stcs.server.entity;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartEntity {
    private int partId;
    private String partDesc;
    private MaterialSpecEntity materialSpec;
    private int partNum;
}
