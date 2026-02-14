package com.harborspace.spacecraft.scoreboard.model;

import com.harborspace.spacecraft.testquestion.model.TestQuestion;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "user_question_score")
@Table(indexes = {@Index(name = "user_question_index",  columnList = "test_question_id,user_id", unique = true)})
public class UserQuestionScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_question_id", updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TestQuestion testQuestion;

    @Column(name = "user_id")
    private String userId;

    @OneToOne
    @JoinColumn(name = "best_submission_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Submission bestSubmission;

    @Override
    public String toString() {
        var testQuestionId = testQuestion != null ? testQuestion.getId().toString() : null;
        var bestSubmissionId = bestSubmission != null ? bestSubmission.getId().toString() : null;
        return "Scoreboard{" +
                "id=" + id +
                ", testQuestionId=" + testQuestionId +
                ", userId=" + userId +
                ", bestSubmissionId=" + bestSubmissionId +
                '}';
    }
}
