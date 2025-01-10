package io.devarium.infrastructure.persistence.entity;

import io.devarium.core.domain.feedback.question.Question;
import io.devarium.core.domain.feedback.question.QuestionType;
import jakarta.persistence.Column;
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
@Table(name = "questions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int orderNumber;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @Column(nullable = false)
    private boolean required;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity project;

    @Builder
    private QuestionEntity(Long id, int orderNumber, String content, QuestionType type,
        boolean required, ProjectEntity project) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.content = content;
        this.type = type;
        this.required = required;
        this.project = project;
    }

    public static QuestionEntity fromDomain(Question question, ProjectEntity project) {
        return QuestionEntity.builder()
            .id(question.getId())
            .orderNumber(question.getOrderNumber())
            .content(question.getContent())
            .type(question.getType())
            .required(question.isRequired())
            .project(project)
            .build();
    }

    public Question toDomain() {
        return Question.builder()
            .id(id)
            .orderNumber(orderNumber)
            .content(content)
            .type(type)
            .required(required)
            .projectId(project.getId())
            .build();
    }

    public void update(Question question) {
        this.orderNumber = question.getOrderNumber();
        this.content = question.getContent();
        this.type = question.getType();
        this.required = question.isRequired();
    }
}
