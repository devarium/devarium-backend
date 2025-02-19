package io.devarium.api.controller.feedback;

import io.devarium.api.auth.CustomUserPrincipal;
import io.devarium.api.common.dto.ListResponse;
import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.feedback.dto.AnswerResponse;
import io.devarium.api.controller.feedback.dto.CreateQuestionRequest;
import io.devarium.api.controller.feedback.dto.FeedbackResponse;
import io.devarium.api.controller.feedback.dto.FeedbackSummaryResponse;
import io.devarium.api.controller.feedback.dto.QuestionResponse;
import io.devarium.api.controller.feedback.dto.SubmitAnswersRequest;
import io.devarium.api.controller.feedback.dto.UpdateQuestionOrdersRequest;
import io.devarium.api.controller.feedback.dto.UpdateQuestionRequest;
import io.devarium.core.domain.feedback.Feedback;
import io.devarium.core.domain.feedback.FeedbackSummary;
import io.devarium.core.domain.feedback.answer.Answer;
import io.devarium.core.domain.feedback.port.in.FeedbackService;
import io.devarium.core.domain.feedback.question.Question;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/projects/{projectId}/feedback")
@RestController
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/questions")
    public ResponseEntity<SingleItemResponse<QuestionResponse>> createFeedbackQuestion(
        @PathVariable Long projectId,
        @Valid @RequestBody CreateQuestionRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Question question = feedbackService.createFeedbackQuestion(
            projectId,
            request,
            principal.getUser()
        );
        QuestionResponse response = QuestionResponse.from(question);

        return ResponseEntity.status(HttpStatus.CREATED).body(SingleItemResponse.from(response));
    }

    @PostMapping("/answers")
    public ResponseEntity<ListResponse<AnswerResponse>> submitFeedbackAnswers(
        @PathVariable Long projectId,
        @Valid @RequestBody SubmitAnswersRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<Answer> answers = feedbackService.submitFeedbackAnswers(
            projectId,
            request,
            principal.getUser()
        );
        List<AnswerResponse> responses = answers.stream().map(AnswerResponse::from).toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(ListResponse.from(responses));
    }

    @GetMapping
    public ResponseEntity<ListResponse<FeedbackResponse>> getFeedback(
        @PathVariable Long projectId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<Feedback> feedbacks = feedbackService.getFeedbacks(projectId, principal.getUser());
        List<FeedbackResponse> responses = feedbacks.stream().map(FeedbackResponse::from).toList();

        return ResponseEntity.ok(ListResponse.from(responses));
    }

    @GetMapping("/questions")
    public ResponseEntity<ListResponse<QuestionResponse>> getFeedbackQuestions(
        @PathVariable Long projectId
    ) {
        List<Question> questions = feedbackService.getFeedbackQuestions(projectId);
        List<QuestionResponse> responses = questions.stream().map(QuestionResponse::from).toList();

        return ResponseEntity.ok(ListResponse.from(responses));
    }

    @GetMapping("/summary")
    public ResponseEntity<ListResponse<FeedbackSummaryResponse>> getFeedbackSummaries(
        @PathVariable Long projectId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<FeedbackSummary> feedbackSummaries = feedbackService.getFeedbackSummaries(
            projectId,
            principal.getUser()
        );
        List<FeedbackSummaryResponse> responses = feedbackSummaries.stream()
            .map(FeedbackSummaryResponse::from)
            .toList();

        return ResponseEntity.ok(ListResponse.from(responses));
    }

    @PatchMapping("/questions/{questionId}")
    public ResponseEntity<SingleItemResponse<QuestionResponse>> updateFeedbackQuestion(
        @PathVariable Long projectId,
        @PathVariable Long questionId,
        @Valid @RequestBody UpdateQuestionRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Question question = feedbackService.updateFeedbackQuestion(
            projectId,
            questionId,
            request,
            principal.getUser()
        );
        QuestionResponse response = QuestionResponse.from(question);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @PatchMapping("/questions/orders")
    public ResponseEntity<ListResponse<QuestionResponse>> updateFeedbackQuestionOrders(
        @PathVariable Long projectId,
        @Valid @RequestBody UpdateQuestionOrdersRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<Question> questions = feedbackService.updateFeedbackQuestionOrders(
            projectId,
            request,
            principal.getUser()
        );
        List<QuestionResponse> responses = questions.stream().map(QuestionResponse::from).toList();

        return ResponseEntity.ok(ListResponse.from(responses));
    }

    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Void> deleteFeedbackQuestion(
        @PathVariable Long projectId,
        @PathVariable Long questionId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        feedbackService.deleteFeedbackQuestion(projectId, questionId, principal.getUser());
        return ResponseEntity.noContent().build();
    }
}
