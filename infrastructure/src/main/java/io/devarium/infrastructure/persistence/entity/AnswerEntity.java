package io.devarium.infrastructure.persistence.entity;

import io.devarium.core.domain.feedback.answer.Answer;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "answers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionEntity question;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Builder
    private AnswerEntity(
        Long id,
        String content,
        QuestionEntity question,
        UserEntity user
    ) {
        this.id = id;
        this.content = content;
        this.question = question;
        this.user = user;
    }

    public static AnswerEntity fromDomain(Answer answer, QuestionEntity question, UserEntity user) {
        return AnswerEntity.builder()
            .id(answer.getId())
            .content(answer.getContent())
            .question(question)
            .user(user)
            .build();
    }

    public Answer toDomain() {
        return Answer.builder()
            .id(id)
            .content(content)
            .questionId(question.getId())
            .userId(user.getId())
            .answeredAt(getUpdatedAt())
            .build();
    }

    public void update(Answer answer) {
        this.content = answer.getContent();
    }
}
