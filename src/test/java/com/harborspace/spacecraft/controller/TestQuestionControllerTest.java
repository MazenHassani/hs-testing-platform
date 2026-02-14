package com.harborspace.spacecraft.controller;

import com.harborspace.spacecraft.exception.DuplicateQuestionException;
import com.harborspace.spacecraft.exception.InvalidRequestException;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.exception.TestHasnotStartedException;
import com.harborspace.spacecraft.question.dto.QuestionDto;
import com.harborspace.spacecraft.question.model.Question;
import com.harborspace.spacecraft.testquestion.dto.TestQuestionDto;
import com.harborspace.spacecraft.testquestion.dto.request.TestQuestionRequest;
import com.harborspace.spacecraft.testquestion.dto.response.TestQuestionResponse;
import com.harborspace.spacecraft.testquestion.service.TestQuestionService;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class TestQuestionControllerTest {
    @Mock
    TestQuestionService testQuestionService;

    @InjectMocks
    TestQuestionController testQuestionController;

    @Test
    public void testFindAllQuestionsInTest() throws ResourceNotFoundException, TestHasnotStartedException {
        // given
        Long courseId = 1L;
        Long testId = 1L;
        List<QuestionDto> questionDtos = List.of(new QuestionDto().setId(1L)
                .setTitle("a title").setQuestionType(Question.QuestionType.SPHERE),
                new QuestionDto().setId(2L)
                        .setTitle("another title").setQuestionType(Question.QuestionType.SPHERE));

        TestQuestionResponse testQuestionResponse = new TestQuestionResponse().setTestId(1L)
                .setQuestionsDtos(questionDtos)
                .setQuestionsWeights(List.of(2, 3));
        Mockito.when(testQuestionService.findAllByTest(1L, false)).thenReturn(testQuestionResponse);

        // when
        ResponseEntity<TestQuestionResponse> result = testQuestionController.findAll(testId,1L);

        // then
        Mockito.verify(testQuestionService, Mockito.times(1)).findAllByTest(1L, false);
        assertNotNull(result);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testCreateTestQuestions() throws ResourceNotFoundException, DuplicateQuestionException, InvalidRequestException {
        // given
        Long courseId = 1L;
        Long testId = 1L;
        List<QuestionDto> questionDtos = List.of(new QuestionDto().setId(1L)
                        .setTitle("a title").setQuestionType(Question.QuestionType.SPHERE),
                new QuestionDto().setId(2L)
                        .setTitle("another title").setQuestionType(Question.QuestionType.SPHERE));
        List<TestQuestionDto> testQuestionDtos = List.of(
                new TestQuestionDto().setTestId(1L).setQuestionId(1L).setWeight(2),
                new TestQuestionDto().setTestId(1L).setQuestionId(2L).setWeight(3)
        );
        TestQuestionRequest testQuestionRequest = new TestQuestionRequest().setTestQuestionDtos(testQuestionDtos).setTestId(testId);

        TestQuestionResponse testQuestionResponse = new TestQuestionResponse().setTestId(1L)
                .setQuestionsDtos(List.of(new QuestionDto().setId(1L), new QuestionDto().setId(2L)))
                .setQuestionsWeights(List.of(2, 3));

        Mockito.when(testQuestionService.save(testQuestionRequest)).thenReturn(testQuestionResponse);

        // when
        ResponseEntity<TestQuestionResponse> result = testQuestionController.save(testQuestionRequest, courseId);

        // then
        Mockito.verify(testQuestionService, Mockito.times(1)).save(any());
        assertNotNull(result);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testUpdateTestQuestions() throws ResourceNotFoundException, DuplicateQuestionException, InvalidRequestException {
        // given
        Long testId = 1L;
        List<QuestionDto> questionDtos = List.of(new QuestionDto().setId(1L)
                        .setTitle("a title").setQuestionType(Question.QuestionType.SPHERE),
                new QuestionDto().setId(2L)
                        .setTitle("another title").setQuestionType(Question.QuestionType.SPHERE));
        List<TestQuestionDto> testQuestionDtos = List.of(
                new TestQuestionDto().setTestId(testId).setQuestionId(1L).setWeight(2),
                new TestQuestionDto().setTestId(testId).setQuestionId(2L).setWeight(3)
        );
        TestQuestionRequest testQuestionRequest = new TestQuestionRequest().setTestQuestionDtos(testQuestionDtos).setTestId(testId);
        TestQuestionResponse testQuestionResponse = new TestQuestionResponse().setTestId(1L)
                .setQuestionsDtos(questionDtos)
                .setQuestionsWeights(List.of(2, 3));

        Mockito.when(testQuestionService.save(testQuestionRequest)).thenReturn(testQuestionResponse);

        // when
        ResponseEntity<TestQuestionResponse> result = testQuestionController.save(testQuestionRequest, 1L);

        // then
        Mockito.verify(testQuestionService, Mockito.times(1)).save(any(TestQuestionRequest.class));
        assertNotNull(result);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testDeleteAllTestQuestions() throws ResourceNotFoundException {
        // given
        Long testId = 1L;

        // when
        ResponseEntity result = testQuestionController.delete(testId, 1L);

        // then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        Mockito.verify(testQuestionService, Mockito.times(1)).deleteAllByTestId(1L);
    }

}
