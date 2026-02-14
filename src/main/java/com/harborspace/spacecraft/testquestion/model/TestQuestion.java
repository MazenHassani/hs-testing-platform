package com.harborspace.spacecraft.testquestion.model;

import com.harborspace.spacecraft.course.model.Test;
import com.harborspace.spacecraft.question.model.Question;
import com.harborspace.spacecraft.student_submission.model.StudentSubmission;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Entity(name="test_question")
@Table(indexes = {@Index(name = "test_question_index",  columnList = "test_id,question_id", unique = true)})
public class TestQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity =  Question.class)
    @JoinColumn(name = "question_id", insertable = false, updatable = false)
    private Question question;

    @Column(name = "question_id")
    private Long questionId;

    @ManyToOne(targetEntity = Test.class)
    @JoinColumn(name = "test_id", insertable = false, updatable = false)
    private Test test;

    @Column(name = "test_id")
    private Long testId;

    private int weight;

    @Column(name = "position")
    private int position;

    @OneToMany(targetEntity = StudentSubmission.class, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "task")
    private List<StudentSubmission> studentSubmissions;
}
