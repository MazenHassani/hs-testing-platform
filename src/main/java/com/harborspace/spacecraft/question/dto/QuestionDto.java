package com.harborspace.spacecraft.question.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.harborspace.spacecraft.question.model.Question;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@ValidQuestion
public class QuestionDto {

    private Long id;

    @NotBlank(message = "Title is mandatory")
    private String title;

    private String statement;

    private String widgetCode;

    @NotNull(message = "Question Type is mandatory")
    private Question.QuestionType questionType;

    private AnswerDto answer;

}