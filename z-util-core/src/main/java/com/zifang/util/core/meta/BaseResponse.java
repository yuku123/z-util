package com.zifang.util.core.meta;

public class BaseResponse {

    private String message;

    private ResultCode code = ResultCode.SUCCESS;

    public BaseResponse() {
    }

    public BaseResponse(String message, ResultCode code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultCode getCode() {
        return code;
    }

    public void setCode(ResultCode code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return code == ResultCode.SUCCESS;
    }

    @Override
    public String toString() {
        return "BaseResponse{message=" + message + ", code=" + code + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseResponse that = (BaseResponse) o;
        return java.util.Objects.equals(message, that.message) &&
                code == that.code;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(message, code);
    }

    public static class Builder {
        private String message;
        private ResultCode code = ResultCode.SUCCESS;

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder code(ResultCode code) {
            this.code = code;
            return this;
        }

        public BaseResponse build() {
            return new BaseResponse(message, code);
        }
    }

    public static BaseResponse builder() {
        return new Builder().build();
    }
}