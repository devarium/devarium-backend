package io.devarium.api.common.dto;

public record ErrorResponse(String title, int status, String detail) {

    public static ErrorResponse of(String title, int status, String detail) {
        return new ErrorResponse(title, status, detail);
    }
}
