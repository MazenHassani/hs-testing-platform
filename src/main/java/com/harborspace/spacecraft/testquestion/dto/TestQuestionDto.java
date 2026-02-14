package com.harborspace.spacecraft.testquestion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.harborspace.spacecraft.question.model.Question.QuestionType;
import com.harborspace.spacecraft.testquestion.model.TestQuestion;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestQuestionDto {
    private Long id;
    private Long testId;
    private Long questionId;
    private String questionTitle;
    private QuestionType questionType;
    private Integer weight;

    public static TestQuestionDto of(TestQuestion tq) {
        return new TestQuestionDto().setId(tq.getId())
                .setQuestionId(tq.getQuestionId()).setQuestionTitle(tq.getQuestion().getTitle())
                .setQuestionType(tq.getQuestion().getQuestionType())
                .setTestId(tq.getTestId()).setWeight(tq.getWeight());
    }
}
