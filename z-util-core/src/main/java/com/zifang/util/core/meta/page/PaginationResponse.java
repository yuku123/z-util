package com.zifang.util.core.meta.page;

import com.zifang.util.core.meta.BaseResponse;
import com.zifang.util.core.meta.ResultCode;

public class PaginationResponse<T> extends BaseResponse {

    private T data;//分页结果
    private int limit;//每页条数
    private int current;//当前页
    private int total;//总数

    public PaginationResponse() {
    }

    public PaginationResponse(String message, ResultCode code, T data, int limit, int current, int total) {
        super(message, code);
        this.data = data;
        this.limit = limit;
        this.current = current;
        this.total = total;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "PaginationResponse{" + super.toString() + ", data=" + data +
                ", limit=" + limit + ", current=" + current + ", total=" + total + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PaginationResponse<?> that = (PaginationResponse<?>) o;
        return limit == that.limit && current == that.current && total == that.total &&
                java.util.Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), data, limit, current, total);
    }

    public static class Builder<T> {
        private String message;
        private ResultCode code = ResultCode.SUCCESS;
        private T data;
        private int limit;
        private int current;
        private int total;

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

        public Builder<T> limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder<T> current(int current) {
            this.current = current;
            return this;
        }

        public Builder<T> total(int total) {
            this.total = total;
            return this;
        }

        public PaginationResponse<T> build() {
            return new PaginationResponse<>(message, code, data, limit, current, total);
        }
    }

    public static <T> PaginationResponse<T> builder() {
        return new Builder<T>().build();
    }
}