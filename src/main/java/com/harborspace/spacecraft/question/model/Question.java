package com.harborspace.spacecraft.question.model;

import com.harborspace.spacecraft.testquestion.model.TestQuestion;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Accessors(chain = true)
@Entity(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String widgetCode;
    @Column(columnDefinition = "TEXT")
    private String statement;
    private QuestionType questionType;

    public enum QuestionType {
        LESSON, SPHERE, MCQ_MULTI, MCQ_SINGLE, MCQ_ORDER, NUMBER
    }


    @OneToOne(targetEntity = Answer.class, orphanRemoval = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "answer_id", insertable = false, updatable = false)
    private Answer answer;

    @OneToMany(targetEntity = TestQuestion.class, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "question")
    private List<TestQuestion> testQuestions;

    @CreatedBy
    private String creator;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

}


