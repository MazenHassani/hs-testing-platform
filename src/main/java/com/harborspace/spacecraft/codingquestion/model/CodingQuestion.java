package com.harborspace.spacecraft.codingquestion.model;

import java.util.List;
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
@Entity(name = "coding_question")
public class CodingQuestion {
    @Id
    private Integer id;

    private String name;
    @Column(columnDefinition = "TEXT")
    private String body; // input + output + legend + notes + scoring
    private Float timeLimit;
    @Builder.Default
    private Integer typeId = 0;
    @Builder.Default
    private Boolean interactive = false;

    @OneToOne(targetEntity = Judge.class, orphanRemoval = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "coding_question_judge_id", insertable = false, updatable = false)
    private Judge judge;

    @OneToOne(targetEntity = Judge.class, orphanRemoval = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "coding_question_tcjudge_id", insertable = false, updatable = false)
    private Judge testCaseJudge;

    @Column(name = "coding_question_judge_id")
    private Integer masterjudgeId;

    @Column(name = "coding_question_tcjudge_id")
    private Integer testCaseJudgeId;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, targetEntity = TestCase.class)
    private List<TestCase> testCases;


}
