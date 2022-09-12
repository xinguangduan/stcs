package org.stcs.server.entity;

import java.util.List;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderEntity {
    private int orderId;
    private String orderDesc;
    private CustomerEntity customer;
    private List<PartEntity> parts;
}