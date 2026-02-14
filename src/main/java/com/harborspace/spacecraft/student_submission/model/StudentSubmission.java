package com.harborspace.spacecraft.student_submission.model;

import com.harborspace.spacecraft.question.model.Answer;
import com.harborspace.spacecraft.question.model.Choice;
import com.harborspace.spacecraft.question.model.Question;
import com.harborspace.spacecraft.question.model.Question.QuestionType;
import com.harborspace.spacecraft.testquestion.model.TestQuestion;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "student_submission")
@Table(indexes = {@Index(name = "student_question_index",  columnList = "student_id, task_id", unique = true)})
public class StudentSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @ManyToOne
    // @JoinColumn(name = "task_id", updatable = false)
    // @OnDelete(action = OnDeleteAction.CASCADE)
    // private TestQuestion task;
    
    @ManyToOne(targetEntity = TestQuestion.class, optional = false)
    @JoinColumn(name = "task_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TestQuestion task;

    @Column(name = "student_id")
    private String studentId;

    private String val;
    
    public Boolean getCorrectVal () {
        Question question = this.getTask().getQuestion();
        QuestionType questionType = question.getQuestionType();
        if (questionType== QuestionType.NUMBER){
            double correctNum = question.getAnswer().getNumber().getValue();
            double studentNumber =Double.parseDouble(val);
            return correctNum==studentNumber;
            //still need precision
        }
        else if (questionType != QuestionType.LESSON ){
            String correctChoice="";
            List <String> correctChoices = new ArrayList<String>();
            Answer answer = question.getAnswer();
            List<Choice> choices= answer.getChoices();
            if (questionType== QuestionType.MCQ_SINGLE){
                for (Choice choice : choices) {
                    if (choice.getSelected()){
                        correctChoice=choice.getStatement();
                    }
                }
                return val==correctChoice;
            }
            else if (questionType== QuestionType.MCQ_MULTI){
                //still needs work
                for (Choice choice : choices) {
                    if (choice.getSelected()){
                        correctChoices.add(choice.getStatement());
                    }
                }
                return val==correctChoice;
            }
            else if (questionType== QuestionType.MCQ_ORDER){
                //still needs work
                return false;
            }
            else return false;
        }
        return false;
    }
}