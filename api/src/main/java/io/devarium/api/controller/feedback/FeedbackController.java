package io.devarium.api.controller.feedback;

import io.devarium.core.domain.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/feedbacks")
@RestController
public class FeedbackController {

    private final FeedbackService feedbackService;
}
