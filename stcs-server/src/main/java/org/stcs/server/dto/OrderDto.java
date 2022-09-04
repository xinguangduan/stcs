package org.stcs.server.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.stcs.server.entity.CustomerEntity;
import org.stcs.server.entity.PartEntity;


@Data
@Document(collection = "order_info")
public class OrderDto implements Serializable {
    private int orderId;
    private String orderDesc;
    private CustomerEntity customer;
    private List<PartEntity> parts;
}