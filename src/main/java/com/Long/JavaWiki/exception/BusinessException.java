package com.Long.JavaWiki.exception;

public class BusinessException extends RuntimeException{

    private EnumCode code;

    public BusinessException (EnumCode code) {
        super(code.getDesc());
        this.code = code;
    }

    public EnumCode getCode() {
        return code;
    }

    public void setCode(EnumCode code) {
        this.code = code;
    }

    /**
     * 不写入堆栈信息，提高性能
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
