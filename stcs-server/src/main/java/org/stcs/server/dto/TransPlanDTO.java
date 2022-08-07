package org.stcs.server.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "trans_plan")
public class TransPlanDTO {
    private int planId;
    private int custId;
    private int orderId;
    private int dockId;
}
