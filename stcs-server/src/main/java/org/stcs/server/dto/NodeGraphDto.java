package org.stcs.server.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "node_graph")
public class NodeGraphDto {
    private String nodeFrom;
    private String nodeTo;
    private int distance;
}
