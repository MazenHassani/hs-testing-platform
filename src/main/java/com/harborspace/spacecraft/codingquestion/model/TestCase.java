package com.harborspace.spacecraft.codingquestion.model;

import javax.persistence.*;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
@Accessors(chain = true)
@Entity(name = "coding_question_tc")
public class TestCase {
    @Id
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String input;

    @Column(columnDefinition = "TEXT")
    private String output;

    @Builder.Default
    private Integer timeLimit = 1;

    private Integer judgeId;

    @Builder.Default
    private Boolean active = true;

    @ManyToOne(targetEntity = CodingQuestion.class)
    @JoinColumn(name = "coding_question_id", nullable = false, updatable = false)
    private CodingQuestion codingQuestion;
}
