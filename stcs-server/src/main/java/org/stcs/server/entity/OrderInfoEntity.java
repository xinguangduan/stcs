package org.stcs.server.entity;

import java.util.Date;

import lombok.Data;


@Data
public class OrderInfoEntity {
    private int orderId;
    private String orderDesc;
    private int custId;
    private int partId;
}