package org.stcs.server.entity;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity {
    private int custId;
    private String custName;
}
