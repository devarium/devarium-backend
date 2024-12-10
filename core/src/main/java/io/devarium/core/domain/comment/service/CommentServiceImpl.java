package io.devarium.core.domain.comment.service;

import io.devarium.core.domain.comment.Comment;
import io.devarium.core.domain.comment.command.UpsertCommentCommand;
import io.devarium.core.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Comment createComment(UpsertCommentCommand command) {
        Comment comment = Comment.builder()
            .content(command.content())
            .build();
        return commentRepository.save(comment);
    }
}
