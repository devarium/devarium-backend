package io.devarium.core.domain.post.service;

import io.devarium.core.domain.comment.service.CommentService;
import io.devarium.core.domain.post.Post;
import io.devarium.core.domain.post.exception.PostErrorCode;
import io.devarium.core.domain.post.exception.PostException;
import io.devarium.core.domain.post.port.UpsertPost;
import io.devarium.core.domain.post.repository.PostRepository;
import io.devarium.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentService commentService;

    @Override
    public Post createPost(UpsertPost request, User user) {
        Post post = Post.builder()
            .title(request.title())
            .content(request.content())
            .userId(user.getId())
            .build();
        return postRepository.save(post);
    }

    @Override
    public Post getPost(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND, postId));
    }

    @Override
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Post updatePost(Long postId, UpsertPost request, User user) {
        Post post = getPost(postId);
        post.updateTitle(request.title());
        post.updateContent(request.content());
        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long postId, User user) {
        commentService.deleteCommentsByPostId(postId);
        postRepository.deleteById(postId);
    }
}
