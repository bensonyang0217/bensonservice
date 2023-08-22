package com.benson.bensonservice.response;

import java.util.Map;
import java.util.StringJoiner;

public class Body {
    private int status = 200;
    private String message = "";

    private Object data;

    public static Body build() {
        return new Body();
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public Body ok(String message) {
        this.status = 200;
        this.message = message;
        return this;
    }

    public Body ok(String message, Object data) {
        this.status = 200;
        this.message = message;
        this.data = data;
        return this;
    }

    public Body ok(String msg, Map<String, Object> data) {
        this.status = 200;
        this.message = msg;
        this.data = data;
        return this;
    }

    public Body fail(String msg) {
        this.status = 400;
        this.message = msg;
        return this;
    }

    public Body fail(String msg, Map<String, Object> data) {
        this.status = 400;
        this.message = msg;
        this.data = data;
        return this;
    }

    public Body internalServerError(String msg) {
        this.status = 500;
        this.message = msg;
        return this;
    }

    public Body internalServerError(String msg, Map<String, Object> data) {
        this.status = 500;
        this.message = msg;
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Body.class.getSimpleName() + "[", "]")
                .add("status=" + status)
                .add("message='" + message + "'")
                .add("data=" + data)
                .toString();
    }
}
