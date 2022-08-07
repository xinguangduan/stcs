package org.stcs.server.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "part_info")
public class PartInfoDTO {
    private int partId;
    private String partDesc;
    private int materId;
    private int partNum;
}
