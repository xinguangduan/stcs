package org.stcs.server.entity;

import lombok.Data;

@Data
public class PartInfoEntity {
    private int partId;
    private String partDesc;
    private int materId;
    private int partNum;
}
