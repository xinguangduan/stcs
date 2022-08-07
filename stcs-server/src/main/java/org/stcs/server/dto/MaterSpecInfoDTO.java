package org.stcs.server.dto;

import java.io.Serializable;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "mater_spec_info")
public class MaterSpecInfoDTO implements Serializable {
    private int materId;
    private String materDesc;
    private String materSpec;
}
