package com.harborspace.spacecraft.scoreboard.model;

import com.harborspace.spacecraft.scoreboard.SubmissionStatus;
import com.harborspace.spacecraft.testquestion.model.TestQuestion;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "submission")
@Table(indexes = {@Index(name = "submission_api_id_index",  columnList = "submissionApiId", unique = true)})
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long submissionApiId;

    private String userId;
    private String recievedName;
    private String recievedEmail;

    @Lob
    private String submissionJson;

    private Boolean isAccepted;
    private Integer score;
    private SubmissionStatus status;

    private Instant creation;
    private Instant update;

    @ManyToOne
    @JoinColumn(name = "test_question_id", updatable = false, insertable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TestQuestion testQuestion;

    @Column(name = "test_question_id")
    private Long testQuestionId;

    @Override
    public String toString() {
        var testQuestionId = testQuestion != null ? testQuestion.getId().toString() : null;
        return "Submission{" +
                "id=" + id +
                ", submissionApiId=" + submissionApiId +
                ", isAccepted=" + isAccepted +
                ", status=" + status +
                ", \nscore=" + score +
                ", studentId=" + userId +
                ", testQuestion=" + testQuestionId +
                ", creationDate=" + creation +
                ", updateDate=" + update +
                ", submissionJson='" + submissionJson + '\'' +
                '}';
    }
}
