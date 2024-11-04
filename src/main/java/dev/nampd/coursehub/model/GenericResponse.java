package dev.nampd.coursehub.model;

import lombok.Data;

@Data
public class GenericResponse<T> {
    private T data;
    private Integer status;
    private String message;
    private Boolean success;


    public GenericResponse() {
        this(null, 200, "success", true);
    }

    public GenericResponse(T data) {
        this(data, 200, "success", true);
    }

    public GenericResponse(T data, Integer code) {
        this(data, code, "success", true);
    }

    public GenericResponse(T data, Integer code, String message) {
        this(data, code, message, true);
    }

    public GenericResponse(T data, Integer status, String message, Boolean success) {
        this.data = data;
        this.status = status;
        this.message = message;
        this.success = success;
    }
}
