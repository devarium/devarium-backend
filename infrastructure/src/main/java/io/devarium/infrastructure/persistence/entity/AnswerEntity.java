package io.devarium.infrastructure.persistence.entity;

import io.devarium.core.domain.feedback.answer.Answer;
import io.devarium.core.domain.feedback.answer.Rating;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    private Rating rating;

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
        Rating rating,
        QuestionEntity question,
        UserEntity user
    ) {
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.question = question;
        this.user = user;
    }

    public static AnswerEntity fromDomain(Answer answer, QuestionEntity question, UserEntity user) {
        return AnswerEntity.builder()
            .id(answer.getId())
            .content(answer.getContent())
            .rating(answer.getRating())
            .question(question)
            .user(user)
            .build();
    }

    public Answer toDomain() {
        return Answer.builder()
            .id(id)
            .content(content)
            .rating(rating)
            .questionId(question.getId())
            .userId(user.getId())
            .answeredAt(getUpdatedAt())
            .build();
    }

    public void update(Answer answer) {
        this.content = answer.getContent();
        this.rating = answer.getRating();
    }
}
