package org.stcs.server.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "trans_path")
public class TransPathDto {
    private int pathId;
    private int partId;
    private int orderId;
    private String pathDef;
    private String currentNode;
}
