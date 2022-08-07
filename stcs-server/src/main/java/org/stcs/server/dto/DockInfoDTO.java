package org.stcs.server.dto;

import java.io.Serializable;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "dock_info")
public class DockInfoDTO implements Serializable {
    private int dockId;
    private String dockDesc;
}
