package io.devarium.api.controller.comment;

import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.comment.dto.CommentResponse;
import io.devarium.api.controller.comment.dto.UpsertCommentRequest;
import io.devarium.core.domain.comment.Comment;
import io.devarium.core.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<SingleItemResponse<CommentResponse>> createComment(
        @RequestBody UpsertCommentRequest upsertCommentRequest
        // TODO: @AuthenticationPrincipal UserDetails userDetails
    ) {
        Comment comment = commentService.createComment((upsertCommentRequest));
        CommentResponse response = CommentResponse.from(comment);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SingleItemResponse.from(response));
    }
}
