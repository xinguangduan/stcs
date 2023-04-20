package org.stcs.server.entity;

import javax.validation.constraints.NotBlank;

import lombok.*;
import org.stcs.server.validation.ValidateIdType;

/**
 * 码头
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DockEntity {
    //custom annotation
    @ValidateIdType
    private int dockId;
    @NotBlank(message = "dockDesc shouldn't be null or empty")
    private String dockDesc;
}
