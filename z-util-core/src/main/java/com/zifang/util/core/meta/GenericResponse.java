package com.zifang.util.core.meta;

public class GenericResponse<T> extends BaseResponse {

    private T data;

    public GenericResponse() {
    }

    public GenericResponse(String message, ResultCode code, T data) {
        super(message, code);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GenericResponse{" + super.toString() + ", data=" + data + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GenericResponse<?> that = (GenericResponse<?>) o;
        return java.util.Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), data);
    }

    public static class Builder<T> {
        private String message;
        private ResultCode code = ResultCode.SUCCESS;
        private T data;

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> code(ResultCode code) {
            this.code = code;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public GenericResponse<T> build() {
            return new GenericResponse<>(message, code, data);
        }
    }

    public static <T> GenericResponse<T> builder() {
        return new Builder<T>().build();
    }
}