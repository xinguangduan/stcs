package org.stcs.server.entity;

import java.util.List;

import lombok.Data;


@Data
public class OrderEntity {
    private int orderId;
    private String orderDesc;
    private CustomerEntity customer;
    private List<PartEntity> parts;
}