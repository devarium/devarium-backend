package io.devarium.core.domain.feedback.question.command;

import java.util.List;

public interface UpdateQuestionOrders {

    List<? extends UpdateQuestionOrder> orderNumbers();

    interface UpdateQuestionOrder {

        Long questionId();

        int orderNumber();
    }
}
