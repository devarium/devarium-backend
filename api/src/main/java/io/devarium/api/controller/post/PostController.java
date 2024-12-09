package io.devarium.api.controller.post;

import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.post.dto.PostResponse;
import io.devarium.api.controller.post.dto.UpsertPostRequest;
import io.devarium.core.domain.post.Post;
import io.devarium.core.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<SingleItemResponse<PostResponse>> createPost(
        @RequestBody UpsertPostRequest upsertPostRequest
        // TODO: @AuthenticationPrincipal UserDetails userDetails
    ) {
        Post post = postService.createPost(upsertPostRequest);
        PostResponse response = PostResponse.from(post);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SingleItemResponse.from(response));
    }
}
