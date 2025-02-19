package io.devarium.api.common;

import io.devarium.api.common.dto.ErrorResponse;
import io.devarium.core.auth.exception.AuthErrorCode;
import io.devarium.core.auth.exception.CustomAuthException;
import io.devarium.core.domain.comment.exception.CommentErrorCode;
import io.devarium.core.domain.comment.exception.CommentException;
import io.devarium.core.domain.feedback.exception.FeedbackErrorCode;
import io.devarium.core.domain.feedback.exception.FeedbackException;
import io.devarium.core.domain.like.exception.LikeErrorCode;
import io.devarium.core.domain.like.exception.LikeException;
import io.devarium.core.domain.membership.exception.MembershipErrorCode;
import io.devarium.core.domain.membership.exception.MembershipException;
import io.devarium.core.domain.post.exception.PostErrorCode;
import io.devarium.core.domain.post.exception.PostException;
import io.devarium.core.domain.project.exception.ProjectErrorCode;
import io.devarium.core.domain.project.exception.ProjectException;
import io.devarium.core.domain.reply.exception.ReplyErrorCode;
import io.devarium.core.domain.reply.exception.ReplyException;
import io.devarium.core.domain.team.exception.TeamErrorCode;
import io.devarium.core.domain.team.exception.TeamException;
import io.devarium.core.domain.teamRequest.exception.TeamRequestErrorCode;
import io.devarium.core.domain.teamRequest.exception.TeamRequestException;
import io.devarium.core.domain.user.exception.UserErrorCode;
import io.devarium.core.domain.user.exception.UserException;
import io.devarium.core.storage.exception.StorageErrorCode;
import io.devarium.core.storage.exception.StorageException;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PostException.class)
    public ResponseEntity<ErrorResponse> handlePostException(PostException e) {
        PostErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.of(
                errorCode.getStatus().name(),
                errorCode.getStatus().value(),
                e.getMessage())
            );
    }

    @ExceptionHandler(CommentException.class)
    public ResponseEntity<ErrorResponse> handleCommentException(CommentException e) {
        CommentErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.of(
                errorCode.getStatus().name(),
                errorCode.getStatus().value(),
                e.getMessage())
            );
    }

    @ExceptionHandler(ReplyException.class)
    public ResponseEntity<ErrorResponse> handleReplyException(ReplyException e) {
        ReplyErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.of(
                errorCode.getStatus().name(),
                errorCode.getStatus().value(),
                e.getMessage())
            );
    }

    @ExceptionHandler(CustomAuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(CustomAuthException e) {
        AuthErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.of(
                errorCode.getStatus().name(),
                errorCode.getStatus().value(),
                e.getMessage())
            );
    }

    @ExceptionHandler(ProjectException.class)
    public ResponseEntity<ErrorResponse> handleProjectException(ProjectException e) {
        ProjectErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.of(
                errorCode.getStatus().name(),
                errorCode.getStatus().value(),
                e.getMessage())
            );
    }

    @ExceptionHandler(FeedbackException.class)
    public ResponseEntity<ErrorResponse> handleFeedbackException(FeedbackException e) {
        FeedbackErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.of(
                errorCode.getStatus().name(),
                errorCode.getStatus().value(),
                e.getMessage())
            );
    }

    @ExceptionHandler(TeamException.class)
    public ResponseEntity<ErrorResponse> handleTeamException(TeamException e) {
        TeamErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.of(
                errorCode.getStatus().name(),
                errorCode.getStatus().value(),
                e.getMessage())
            );
    }

    @ExceptionHandler(MembershipException.class)
    public ResponseEntity<ErrorResponse> handlerMembershipException(MembershipException e) {
        MembershipErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.of(
                errorCode.getStatus().name(),
                errorCode.getStatus().value(),
                e.getMessage())
            );
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorResponse> handleStorageException(StorageException e) {
        StorageErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.of(
                errorCode.getStatus().name(),
                errorCode.getStatus().value(),
                e.getMessage())
            );
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserException e) {
        UserErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.of(
                errorCode.getStatus().name(),
                errorCode.getStatus().value(),
                e.getMessage())
            );
    }

    @ExceptionHandler(TeamRequestException.class)
    public ResponseEntity<ErrorResponse> handleTeamRequestException(TeamRequestException e) {
        TeamRequestErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.of(
                errorCode.getStatus().name(),
                errorCode.getStatus().value(),
                e.getMessage())
            );
    }

    @ExceptionHandler(LikeException.class)
    public ResponseEntity<ErrorResponse> handleLikeException(LikeException e) {
        LikeErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ErrorResponse.of(
                errorCode.getStatus().name(),
                errorCode.getStatus().value(),
                e.getMessage())
            );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e
    ) {
        List<String> errorMessages = Stream.concat(
                e.getBindingResult().getFieldErrors().stream(),
                e.getBindingResult().getGlobalErrors().stream()
            )
            .map(ObjectError::getDefaultMessage)
            .toList();

        String combinedErrorMessage = String.join("\n", errorMessages);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ErrorResponse(
                HttpStatus.BAD_REQUEST.name(),
                HttpStatus.BAD_REQUEST.value(),
                combinedErrorMessage
            )
        );
    }
}
