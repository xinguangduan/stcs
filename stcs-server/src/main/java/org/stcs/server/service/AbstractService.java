package org.stcs.server.service;

import org.stcs.server.constant.ResultType;
import org.stcs.server.exception.STCSException;
import org.stcs.server.exception.STCSExceptionEntity;

public abstract class AbstractService {

    public <T> void checkNotFoundException(T t) throws STCSException {
        if (t == null) {
            STCSExceptionEntity exceptionEntity = STCSExceptionEntity.builder()
                    .code(ResultType.RECORD_NOT_FOUND.getCode())
                    .reason(ResultType.RECORD_NOT_FOUND.getInfo())
                    .description(ResultType.RECORD_NOT_FOUND.getDescription())
                    .build();
            throw new STCSException(exceptionEntity);
        }
    }
}
