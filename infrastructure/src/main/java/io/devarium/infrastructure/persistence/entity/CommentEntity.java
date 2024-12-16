package io.devarium.infrastructure.persistence.entity;

import io.devarium.core.domain.comment.Comment;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ReplyEntity> replies = new ArrayList<>();

    @Builder
    private CommentEntity(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public void update(Comment domain) {
        this.content = domain.getContent();
    }

    public void addReply(ReplyEntity reply) {
        replies.add(reply);
        reply.setComment(this);
    }

    public void removeReply(ReplyEntity reply) {
        replies.remove(reply);
        reply.setComment(null);
    }
}
