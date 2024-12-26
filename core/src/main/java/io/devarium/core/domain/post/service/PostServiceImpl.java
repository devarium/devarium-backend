package io.devarium.core.domain.post.service;

import io.devarium.core.domain.comment.service.CommentService;
import io.devarium.core.domain.post.Post;
import io.devarium.core.domain.post.exception.PostErrorCode;
import io.devarium.core.domain.post.exception.PostException;
import io.devarium.core.domain.post.port.UpsertPost;
import io.devarium.core.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentService commentService;

    @Override
    public Post createPost(UpsertPost request) {
        Post post = Post.builder()
            .title(request.title())
            .content(request.content())
            .build();
        return postRepository.save(post);
    }

    @Override
    public Post getPost(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND, postId));
    }

    @Override
    public Post updatePost(Long postId, UpsertPost request) {
        Post post = getPost(postId);
        post.updateTitle(request.title());
        post.updateContent(request.content());
        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long postId) {
        commentService.deleteCommentsByPostId(postId);
        postRepository.deleteById(postId);
    }
}
