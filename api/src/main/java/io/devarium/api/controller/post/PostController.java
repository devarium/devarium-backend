package io.devarium.api.controller.post;

import io.devarium.api.auth.CustomUserPrincipal;
import io.devarium.api.common.dto.PagedListResponse;
import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.comment.dto.CommentResponse;
import io.devarium.api.controller.post.dto.PostResponse;
import io.devarium.api.controller.post.dto.UpsertPostRequest;
import io.devarium.core.domain.comment.Comment;
import io.devarium.core.domain.comment.port.in.CommentService;
import io.devarium.core.domain.like.port.in.LikeService;
import io.devarium.core.domain.post.Post;
import io.devarium.core.domain.post.port.in.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/posts")
@RestController
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<SingleItemResponse<PostResponse>> createPost(
        @Valid @RequestBody UpsertPostRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Post post = postService.createPost(request, principal.getUser());
        PostResponse response = createPostResponse(post, principal);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SingleItemResponse.from(response));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<SingleItemResponse<PostResponse>> getPost(
        @PathVariable Long postId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Post post = postService.getPost(postId);
        PostResponse response = createPostResponse(post, principal);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    // TODO: 책임분리 및 URL 구조 논의 필요
    @GetMapping("/{postId}/comments")
    public ResponseEntity<PagedListResponse<CommentResponse>> getCommentsByPostId(
        @PathVariable Long postId,
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @PageableDefault(size = Comment.DEFAULT_PAGE_SIZE, sort = "createdAt", direction = Direction.ASC) Pageable pageable
    ) {
        Page<Comment> comments = commentService.getCommentsByPostId(postId, pageable);
        Page<CommentResponse> commentResponses = comments.map(comment -> {
            Long likeCount = likeService.getLikeCount(comment);
            boolean userLiked =
                principal != null && likeService.hasUserLiked(comment, principal.getUser());
            return CommentResponse.of(comment, likeCount, userLiked);
        });

        return ResponseEntity.ok(PagedListResponse.from(commentResponses));
    }

    @GetMapping("/all")
    public ResponseEntity<PagedListResponse<PostResponse>> getAllPosts(
        @AuthenticationPrincipal CustomUserPrincipal principal,
        @PageableDefault(size = Post.DEFAULT_PAGE_SIZE, sort = "createdAt", direction = Direction.DESC) Pageable pageable
    ) {
        Page<Post> posts = postService.getAllPosts(pageable);
        Page<PostResponse> postResponses = posts.map(post -> createPostResponse(post, principal));

        return ResponseEntity.ok(PagedListResponse.from(postResponses));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<SingleItemResponse<PostResponse>> updatePost(
        @PathVariable Long postId,
        @Valid @RequestBody UpsertPostRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Post post = postService.updatePost(postId, request, principal.getUser());
        PostResponse response = createPostResponse(post, principal);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
        @PathVariable Long postId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        postService.deletePost(postId, principal.getUser());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<Void> likePost(
        @PathVariable Long postId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Post post = postService.getPost(postId);
        likeService.like(post, principal.getUser());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<Void> unlikePost(
        @PathVariable Long postId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Post post = postService.getPost(postId);
        likeService.unlike(post, principal.getUser());
        return ResponseEntity.noContent().build();
    }

    private PostResponse createPostResponse(Post post, CustomUserPrincipal principal) {
        Long likeCount = likeService.getLikeCount(post);
        boolean userLiked =
            principal != null && likeService.hasUserLiked(post, principal.getUser());
        return PostResponse.of(post, likeCount, userLiked);
    }
}
