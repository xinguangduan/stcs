package org.stcs.server.dto;

import java.io.Serializable;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "material_spec_info")
public class MaterialSpecDto implements Serializable {
    private int materialId;
    private String materialDesc;
    private String materialSpec;
}
