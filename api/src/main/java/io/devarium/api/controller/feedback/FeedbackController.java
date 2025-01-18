package io.devarium.api.controller.feedback;

import io.devarium.api.auth.CustomUserPrincipal;
import io.devarium.api.common.dto.ListResponse;
import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.feedback.dto.AnswerResponse;
import io.devarium.api.controller.feedback.dto.CreateQuestionRequest;
import io.devarium.api.controller.feedback.dto.FeedbackResponse;
import io.devarium.api.controller.feedback.dto.QuestionResponse;
import io.devarium.api.controller.feedback.dto.SubmitAnswersRequest;
import io.devarium.api.controller.feedback.dto.SyncQuestionsRequest;
import io.devarium.api.controller.feedback.dto.UpdateQuestionOrdersRequest;
import io.devarium.api.controller.feedback.dto.UpdateQuestionRequest;
import io.devarium.core.domain.feedback.Feedback;
import io.devarium.core.domain.feedback.answer.Answer;
import io.devarium.core.domain.feedback.question.Question;
import io.devarium.core.domain.feedback.service.FeedbackService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/projects/{projectId}/feedback")
@RestController
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/questions")
    public ResponseEntity<Void> createFeedbackQuestion(
        @PathVariable Long projectId,
        @Valid @RequestBody CreateQuestionRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return null;
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
    public ResponseEntity<SingleItemResponse<FeedbackResponse>> getFeedback(
        @PathVariable Long projectId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Feedback feedback = feedbackService.getFeedback(projectId, principal.getUser());
        FeedbackResponse response = FeedbackResponse.from(feedback);

        return ResponseEntity.ok(SingleItemResponse.from(response));
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
    public ResponseEntity<ListResponse<AnswerResponse>> getFeedbackSummary(
        @PathVariable Long projectId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        // 제출된 답변 요약 조회
        // TODO: 객관식 점수 평균 및 주관식 AI 요약 기능
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
    }

    @PatchMapping("/questions/{questionId}")
    public ResponseEntity<Void> updateFeedbackQuestion(
        @PathVariable Long projectId,
        @PathVariable Long questionId,
        @Valid @RequestBody UpdateQuestionRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return null;
    }

    @PatchMapping("/questions/orders")
    public ResponseEntity<Void> updateQuestionsOrder(
        @PathVariable Long projectId,
        @Valid @RequestBody UpdateQuestionOrdersRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return null;
    }

    @PutMapping("/questions")
    public ResponseEntity<ListResponse<QuestionResponse>> syncFeedbackQuestions(
        @PathVariable Long projectId,
        @Valid @RequestBody SyncQuestionsRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<Question> questions = feedbackService.syncFeedbackQuestions(
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
        return null;
    }
}
