package org.stcs.server.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "cust_info")
public class CustomerDto {
    private int custId;
    private String custName;
}
