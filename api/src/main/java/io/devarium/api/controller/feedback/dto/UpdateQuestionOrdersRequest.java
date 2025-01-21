package io.devarium.api.controller.feedback.dto;

import io.devarium.core.domain.feedback.question.port.UpdateQuestionOrder;
import io.devarium.core.domain.feedback.question.port.UpdateQuestionOrders;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record UpdateQuestionOrdersRequest(
    @NotNull(message = "'orderNumbers' must not be null") List<UpdateQuestionOrderRequest> orderNumbers
) implements UpdateQuestionOrders {

    public record UpdateQuestionOrderRequest(
        @NotNull(message = "'questionId' must not be null") Long questionId,
        @Positive(message = "'orderNumber' must be greater than 0") int orderNumber
    ) implements UpdateQuestionOrder {

    }
}
