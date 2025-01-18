package io.devarium.api.controller.feedback.dto;

import java.util.List;

public record UpdateQuestionOrdersRequest(
    List<UpdateQuestionOrderRequest> orderNumbers
) {

    public record UpdateQuestionOrderRequest(
        Long questionId,
        int orderNumber
    ) {

    }
}
