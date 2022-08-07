package org.stcs.server.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "order_info")
public class OrderInfoDTO implements Serializable {
    private int orderId;
    private String orderDesc;
    private int custId;
    private int partId;
}