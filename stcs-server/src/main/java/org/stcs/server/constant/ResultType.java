package org.stcs.server.constant;

public enum ResultType {
    DELETE_SUCCESS("ok", "delete success", ""),
    UPDATE_SUCCESS("ok", "update success", ""),
    CUSTOMER_ADD_FAILURE("1001", "customer add failure", ""),
    CUSTOMER_DELETE_FAILURE("1002", "customer delete failure", ""),
    CUSTOMER_UPDATE_FAILURE("1003", "customer update failure", ""),
    RECORD_NOT_FOUND("1005", "not found the record", "not found the record"),
    TRANSPATH_UPDATE_FAILURE("1001","trans path update failure", "");

    private final String code;
    private final String info;
    private final String description;

    ResultType(String code, String info, String description) {
        this.code = code;
        this.info = info;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public String getDescription() {
        return description;
    }
}
