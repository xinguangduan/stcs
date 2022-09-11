package org.stcs.server.service;

import static org.stcs.server.constant.GlobalConstant.ERROR_1005;

import org.stcs.server.exception.STCSException;
import org.stcs.server.exception.STCSExceptionEntity;

public abstract class AbstractService {

    public <T> void checkNotFoundException(T t) throws STCSException {
        if (t == null) {
            STCSExceptionEntity exceptionEntity = STCSExceptionEntity.builder()
                    .code(ERROR_1005)
                    .reason("not found the record")
                    .description("not found the record")
                    .build();
            throw new STCSException(exceptionEntity);
        }
    }
}
