package io.devarium.api.controller.feedback;

import io.devarium.api.common.dto.ListResponse;
import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.feedback.dto.AnswerResponse;
import io.devarium.api.controller.feedback.dto.CreateQuestionsRequest;
import io.devarium.api.controller.feedback.dto.FeedbackResponse;
import io.devarium.api.controller.feedback.dto.QuestionResponse;
import io.devarium.api.controller.feedback.dto.SubmitAnswersRequest;
import io.devarium.core.auth.EmailPrincipal;
import io.devarium.core.domain.feedback.question.Question;
import io.devarium.core.domain.feedback.service.FeedbackService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/projects/{projectId}/feedbacks")
@RestController
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/questions")
    public ResponseEntity<ListResponse<QuestionResponse>> createFeedbackQuestions(
        @PathVariable Long projectId,
        @Valid @RequestBody CreateQuestionsRequest request,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        List<Question> questions = feedbackService.createFeedbackQuestions(
            projectId,
            request,
            emailPrincipal.getUser()
        );
        List<QuestionResponse> responses = questions.stream().map(QuestionResponse::from).toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(ListResponse.from(responses));
    }

    @PostMapping("/answers")
    public ResponseEntity<ListResponse<AnswerResponse>> submitFeedbackAnswers(
        @PathVariable Long projectId,
        @Valid @RequestBody SubmitAnswersRequest request,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        // 피드백 답변 제출
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
    }

    @GetMapping
    public ResponseEntity<SingleItemResponse<FeedbackResponse>> getFeedback(
        @PathVariable Long projectId,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
    }

    @GetMapping("/questions")
    public ResponseEntity<ListResponse<QuestionResponse>> getFeedbackQuestions(
        @PathVariable Long projectId,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        // 피드백 질문들 조회
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
    }

    @GetMapping("/answers")
    public ResponseEntity<ListResponse<AnswerResponse>> getFeedbackAnswers(
        @PathVariable Long projectId,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        // 제출된 답변들 조회
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
    }
}
