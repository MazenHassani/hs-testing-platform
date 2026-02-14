package com.harborspace.spacecraft.controller;

import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.question.dto.AnswerDto;
import com.harborspace.spacecraft.question.dto.ChoiceDto;
import com.harborspace.spacecraft.question.dto.QuestionDto;
import com.harborspace.spacecraft.question.model.Choice;
import com.harborspace.spacecraft.question.model.Question;
import com.harborspace.spacecraft.question.service.AnswerService;
import com.harborspace.spacecraft.question.service.QuestionService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class QuestionControllerTest {
    @InjectMocks
    QuestionController questionController;

    @Mock
    QuestionService questionService;

    @Mock
    AnswerService answerService;

    @Test
    public void testFindOne() throws ResourceNotFoundException {
        // given
        QuestionDto question =
                new QuestionDto().setId(1L).setTitle("Hello World").setQuestionType(Question.QuestionType.SPHERE);
        Mockito.when(questionService.findOne(1L)).thenReturn(question);

        // when
        QuestionDto result = questionController.findOne(1L).getBody();

        // then
        Mockito.verify(questionService, Mockito.times(1)).findOne(1L);
        assertThat(result).isNotNull();
    }

    @Test
    public void testFindAll()
    {
        // given
        QuestionDto question = new QuestionDto()
                .setId(1L)
                .setTitle("Test Question")
                .setQuestionType(Question.QuestionType.SPHERE)
                .setStatement("a statement");

        // when
        Mockito.when(questionService.findAll("1")).thenReturn(List.of(question));
        ResponseEntity<List<QuestionDto>> result1 = questionController.findAll("1");

        Mockito.when(questionService.findAll("1")).thenReturn(List.of());
        ResponseEntity<List<QuestionDto>> result2 = questionController.findAll("1");

        // then
        assertThat(result1).isNotNull();
        assertThat(result1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result1.getBody().size()).isEqualTo(1);

        assertThat(result2).isNotNull();
        assertThat(result2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result2.getBody().size()).isEqualTo(0);
    }

    @Test
    public void testCreateQuestionValid() {
        // given
        QuestionDto questionDto = new QuestionDto()
                .setTitle("Easy Question")
                .setQuestionType(Question.QuestionType.SPHERE);
        Mockito.when(questionService.create(questionDto)).thenReturn(questionDto.setId(1L));
        Mockito.when(answerService.create(questionDto.getAnswer())).thenReturn(questionDto.getAnswer());

        // when
        QuestionDto result = questionController.createQuestion(questionDto).getBody();

        // then
        assertThat(result).isNotNull();
        Mockito.verify(questionService, Mockito.times(1)).create(questionDto);
    }

    @Test
    public void testCreateQuestionWithAnswerValid() {
        // given
        ChoiceDto c1Dto = new ChoiceDto().setId(1L).setStatement("opt1");
        ChoiceDto c2Dto = new ChoiceDto().setId(2L).setStatement("opt2");
        List<ChoiceDto> choicesDto = List.of(c1Dto, c2Dto);
        AnswerDto answerDto = new AnswerDto().setId(1L).setChoices(choicesDto);
        QuestionDto questionDto = new QuestionDto()
                .setTitle("Easy Question")
                .setQuestionType(Question.QuestionType.SPHERE)
                .setAnswer(answerDto);
        Mockito.when(questionService.create(questionDto)).thenReturn(questionDto.setId(1L));
        Mockito.when(answerService.create(questionDto.getAnswer())).thenReturn(questionDto.getAnswer());

        // when
        QuestionDto result = questionController.createQuestion(questionDto).getBody();

        // then
        assertThat(result).isNotNull();
        Mockito.verify(questionService, Mockito.times(1)).create(questionDto);
        Mockito.verify(answerService, Mockito.times(1)).create(questionDto.getAnswer());
    }

    @Test
    public void testUpdateQuestionValid() throws ResourceNotFoundException {
        // given
        QuestionDto questionDto = new QuestionDto()
                .setTitle("Easy Question")
                .setQuestionType(Question.QuestionType.SPHERE);;
        Mockito.when(questionService.update(questionDto)).thenReturn(questionDto);
        Mockito.when(answerService.update(questionDto.getAnswer())).thenReturn(questionDto.getAnswer());

        // when
        QuestionDto result = questionController.updateQuestion(1L, questionDto).getBody();

        // then
        assertThat(result).isNotNull();
        Mockito.verify(questionService, Mockito.times(1)).update(questionDto);
    }

    @Test
    public void testUpdateQuestionWithAnswerValid() throws ResourceNotFoundException {
        // given
        ChoiceDto c1Dto = new ChoiceDto().setId(1L).setStatement("opt1");
        ChoiceDto c2Dto = new ChoiceDto().setId(2L).setStatement("opt2");
        List<ChoiceDto> choicesDto = List.of(c1Dto, c2Dto);
        AnswerDto answerDto = new AnswerDto().setId(1L).setChoices(choicesDto);
        QuestionDto questionDto = new QuestionDto()
                .setTitle("Easy Question")
                .setQuestionType(Question.QuestionType.SPHERE)
                .setAnswer(answerDto);
        Mockito.when(questionService.update(questionDto)).thenReturn(questionDto);
        Mockito.when(answerService.update(questionDto.getAnswer())).thenReturn(questionDto.getAnswer());

        // when
        QuestionDto result = questionController.updateQuestion(1L, questionDto).getBody();

        // then
        assertThat(result).isNotNull();
        Mockito.verify(questionService, Mockito.times(1)).update(questionDto);
        Mockito.verify(answerService, Mockito.times(1)).update(questionDto.getAnswer());
    }

    @Test
    public void testDeleteQuestionValid() {
        // given
        Long idToDelete = 1L;

        // when
        ResponseEntity result = questionController.deleteQuestion(idToDelete);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        Mockito.verify(questionService, Mockito.times(1)).deleteById(1L);

    }



}
