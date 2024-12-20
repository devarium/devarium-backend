package io.devarium.api.controller.reply;

import io.devarium.api.common.dto.PagedListResponse;
import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.reply.dto.ReplyResponse;
import io.devarium.api.controller.reply.dto.UpsertReplyRequest;
import io.devarium.core.domain.reply.Reply;
import io.devarium.core.domain.reply.service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/replies")
@RestController
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<SingleItemResponse<ReplyResponse>> createReply(
        @Valid @RequestBody UpsertReplyRequest request,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        Reply reply = replyService.createReply(request);
        ReplyResponse response = ReplyResponse.from(reply);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SingleItemResponse.from(response));
    }

    @GetMapping("/{replyId}")
    public ResponseEntity<SingleItemResponse<ReplyResponse>> getReply(@PathVariable Long replyId) {
        Reply reply = replyService.getReply(replyId);
        ReplyResponse response = ReplyResponse.from(reply);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @GetMapping("/all/{commentId}")
    public ResponseEntity<PagedListResponse<Reply>> getRepliesByCommentId(
        @PathVariable Long commentId,
        @PageableDefault(size = 10, sort = "createdAt", direction = Direction.ASC) Pageable pageable
    ) {
        Page<Reply> replies = replyService.getRepliesByCommentId(commentId, pageable);

        return ResponseEntity.ok(PagedListResponse.from(replies));
    }

    @PutMapping("/{replyId}")
    public ResponseEntity<SingleItemResponse<ReplyResponse>> updateReply(
        @PathVariable Long replyId,
        @Valid @RequestBody UpsertReplyRequest request,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        Reply reply = replyService.updateReply(replyId, request);
        ReplyResponse response = ReplyResponse.from(reply);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(
        @PathVariable Long replyId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        replyService.deleteReply(replyId);
        return ResponseEntity.noContent().build();
    }
}
