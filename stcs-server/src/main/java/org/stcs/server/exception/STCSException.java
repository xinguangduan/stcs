package org.stcs.server.exception;

import lombok.Getter;
import lombok.NonNull;

public class STCSException extends Exception {

    @Getter
    private final org.stcs.server.exception.STCSExceptionEntity STCSExceptionEntity;

    public STCSException(@NonNull STCSExceptionEntity messageObject) {
        STCSExceptionEntity = messageObject;
    }

}
