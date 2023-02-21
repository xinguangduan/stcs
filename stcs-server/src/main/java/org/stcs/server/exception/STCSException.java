package org.stcs.server.exception;

import lombok.Getter;
import lombok.NonNull;

public class STCSException extends RuntimeException {

    @Getter
    private final STCSExceptionEntity STCSExceptionEntity;

    public STCSException(@NonNull STCSExceptionEntity messageObject) {
        STCSExceptionEntity = messageObject;
    }

}
