package io.devarium.api.common.dto;

import java.util.List;

public record ListResponse<T>(List<T> data) {

    public static <T> ListResponse<T> from(List<T> data) {
        return new ListResponse<>(data);
    }
}
