package org.stcs.server.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "part_info")
public class PartDto {
    private int partId;
    private String partDesc;
    private MaterialSpecDto materialSpec;
    private int partNum;
}
