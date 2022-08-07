package org.stcs.server.exception;

import lombok.Getter;
import lombok.NonNull;
import org.stcs.server.entity.STCSExceptionEntity;

public class STCSException extends Exception {

    @Getter
    private final org.stcs.server.entity.STCSExceptionEntity STCSExceptionEntity;

    public STCSException(@NonNull STCSExceptionEntity messageObject) {
        STCSExceptionEntity = messageObject;
    }

}
