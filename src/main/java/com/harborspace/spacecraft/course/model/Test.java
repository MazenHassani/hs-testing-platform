package com.harborspace.spacecraft.course.model;

import com.harborspace.spacecraft.student_submission.StudentScore;
import com.harborspace.spacecraft.student_submission.TaskScore;
import com.harborspace.spacecraft.student_submission.TestScore;
import com.harborspace.spacecraft.student_submission.model.StudentSubmission;
import com.harborspace.spacecraft.testquestion.model.TestQuestion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "test")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    private Long duration;

    private Boolean resultsVisibility;

    private Integer weight;

    @OneToMany(targetEntity = TestQuestion.class, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "test")
    private List<TestQuestion> testQuestions;

    @ManyToOne
    @JoinColumn
    private Course course;

    @CreatedBy
    private String creator;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;
    
    public List<StudentScore> getTestScores() {
        List<TestQuestion> tasks = this.getTestQuestions();
        List<StudentScore> scores = new ArrayList<>();
      
        for (TestQuestion task : tasks) {
            List<StudentSubmission> submissions = task.getStudentSubmissions();
            
            for (StudentSubmission studentSubmission : submissions) {
                
                Optional <StudentScore> studentScoreOpt = scores.stream()
                        .filter(s -> studentSubmission.getStudentId().equals(s.getStudentId())).findFirst();
                final StudentScore studentScore;
                if (studentScoreOpt.isPresent())
                {
                    studentScore = studentScoreOpt.get();
                    
                }
                else { 
                    studentScore = new StudentScore();
                    studentScore.setStudentId(studentSubmission.getStudentId());}
                    
                final TestScore testScore;
                    Optional <TestScore> testScoreOpt = studentScore.getTestsScore().stream()
                        .filter(s -> this.getId().equals(s.getTestId())).findFirst();
                if (testScoreOpt.isEmpty())
                {
                    testScore = new TestScore();
                    testScore.setTestId(this.getId());
                }
                else testScore = testScoreOpt.get();

                TaskScore taskScore = new TaskScore();
                taskScore.setTaskId(task.getId());
                taskScore.setScore(studentSubmission.getCorrectVal() ? task.getWeight() : 0);

                testScore.getTasksScore().add(taskScore);
                testScore.setTasksScore(testScore.getTasksScore());

                boolean conditioned = studentScore.getTestsScore().stream()
                .filter(s -> this.getId().equals(s.getTestId())).findFirst().isEmpty();

                if (conditioned) studentScore.getTestsScore().add(testScore);
                studentScore.setTestsScore(studentScore.getTestsScore());
                
                conditioned = scores.stream()
                        .filter(s -> studentScore.getStudentId().equals(s.getStudentId())).findFirst().isEmpty();

               if (conditioned) scores.add(studentScore);
            }
        }
        return scores;
    }
}
