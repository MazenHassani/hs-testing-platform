package com.harborspace.spacecraft.service;

import com.harborspace.spacecraft.course.repository.TestRepository;
import com.harborspace.spacecraft.exception.DuplicateQuestionException;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.exception.TestHasnotStartedException;
import com.harborspace.spacecraft.question.model.Question;
import com.harborspace.spacecraft.question.repository.QuestionRepository;
import com.harborspace.spacecraft.testquestion.dto.TestQuestionDto;
import com.harborspace.spacecraft.testquestion.model.TestQuestion;
import com.harborspace.spacecraft.testquestion.repository.TestQuestionRepository;
import com.harborspace.spacecraft.testquestion.dto.request.TestQuestionRequest;
import com.harborspace.spacecraft.testquestion.dto.response.TestQuestionResponse;
import com.harborspace.spacecraft.testquestion.service.impl.TestQuestionServiceImpl;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TestQuestionServiceTest {

    @Mock
    TestQuestionRepository testQuestionRepository;

    @Mock
    QuestionRepository questionRepository;

    @Mock
    TestRepository testRepository;

    @Spy
    ModelMapper modelMapper;

    @InjectMocks
    TestQuestionServiceImpl testQuestionService;

    @Test
    public void createOneTestQuestionSuccess() throws ResourceNotFoundException, DuplicateQuestionException {
        //given
        List<TestQuestionDto> testQuestionDtos = List.of(new TestQuestionDto().setTestId(2L)
        .setQuestionId(1L)
        .setWeight(3));

        Mockito.when(testRepository.existsById(2L)).thenReturn(true);
        Mockito.when(questionRepository.countAllByIdIn(anyList())).thenReturn(1);

        //when
        TestQuestionResponse response = testQuestionService.save(new TestQuestionRequest()
                .setTestQuestionDtos(testQuestionDtos).setTestId(2L));

        //then
        Mockito.verify(testQuestionRepository, times(1)).saveAll(any());
        assertThat(response).isNotNull();
    }

    @Test
    public void createManyTestQuestionsSuccess() throws ResourceNotFoundException, DuplicateQuestionException {
        //given
        Mockito.when(testRepository.existsById(any())).thenReturn(true);
        Mockito.when(questionRepository.countAllByIdIn(any())).thenReturn(3);
        Long testId = 1L;
        List<Long> questionsIds = List.of(1L, 2L, 3L);
        List<Integer> questionsWeights = List.of(3, 2, 5);
        List<TestQuestionDto> testQuestionDtos = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            testQuestionDtos.add(
                    new TestQuestionDto().setQuestionId(questionsIds.get(i))
                    .setTestId(testId)
                    .setWeight(questionsWeights.get(i))
            );
        }
        //when
        TestQuestionResponse response = testQuestionService.save(new TestQuestionRequest()
                .setTestQuestionDtos(testQuestionDtos).setTestId(testId));

        //then
        Mockito.verify(testQuestionRepository, times(1)).saveAll(any());
        assertThat(response).isNotNull();
    }

    @Test
    public void findAllTestQuestionsSuccess() throws ResourceNotFoundException, TestHasnotStartedException {
        //given
        List<TestQuestion> testQuestions = new ArrayList<>();
        var test = new com.harborspace.spacecraft.course.model.Test().setId(1L).setStartDate(new Date());
        var question1 = new Question().setId(1L).setTitle("Title").setQuestionType(Question.QuestionType.SPHERE);
        var question2 = new Question().setId(2L).setTitle("Title").setQuestionType(Question.QuestionType.SPHERE);
        var question3 = new Question().setId(3L).setTitle("Title").setQuestionType(Question.QuestionType.SPHERE);
        testQuestions.add(new TestQuestion().setId(1L).setQuestionId(1L).setQuestion(question1).setTest(test).setPosition(1));
        testQuestions.add(new TestQuestion().setId(2L).setQuestionId(3L).setQuestion(question3).setTest(test).setPosition(0));
        testQuestions.add(new TestQuestion().setId(3L).setQuestionId(2L).setQuestion(question2).setTest(test).setPosition(2));
        Mockito.when(testRepository.findById(1L)).thenReturn(Optional.of(test));


        Mockito.when(testQuestionRepository.findAllByTestId(1L)).thenReturn(testQuestions);

        //when
        TestQuestionResponse result = testQuestionService.findAllByTest(1, false);

        //then
        Mockito.verify(testQuestionRepository, times(1)).findAllByTestId(1L);
        assertThat(result.getQuestionsDtos().get(0).getId()).isEqualTo(3L);
        assertThat(result.getQuestionsDtos().get(1).getId()).isEqualTo(1L);
        assertThat(result.getQuestionsDtos().get(2).getId()).isEqualTo(2L);
        assertThat(result).isNotNull();
    }

    @Test
    public void findAllTestQuestionsStudentNotStarted() throws ResourceNotFoundException, TestHasnotStartedException {
        //given
        List<TestQuestion> testQuestions = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, 5);
        var test = new com.harborspace.spacecraft.course.model.Test().setId(1L).setStartDate(cal.getTime());
        var question1 = new Question().setId(1L).setTitle("Title").setQuestionType(Question.QuestionType.SPHERE);
        var question2 = new Question().setId(2L).setTitle("Title").setQuestionType(Question.QuestionType.SPHERE);
        var question3 = new Question().setId(3L).setTitle("Title").setQuestionType(Question.QuestionType.SPHERE);
        testQuestions.add(new TestQuestion().setId(1L).setQuestionId(1L).setQuestion(question1).setTest(test).setPosition(1));
        testQuestions.add(new TestQuestion().setId(2L).setQuestionId(3L).setQuestion(question3).setTest(test).setPosition(0));
        testQuestions.add(new TestQuestion().setId(3L).setQuestionId(2L).setQuestion(question2).setTest(test).setPosition(2));
        Mockito.when(testRepository.findById(1L)).thenReturn(Optional.of(test));

        //when
        assertThrows(TestHasnotStartedException.class, () -> testQuestionService.findAllByTest(1, true));

        //then
        Mockito.verify(testQuestionRepository, times(0)).findAllByTestId(1L);
    }

    @Test
    public void updateTestQuestions() throws ResourceNotFoundException, DuplicateQuestionException {
        //given
        List<TestQuestion> testQuestions = new ArrayList<>();
        var test = new com.harborspace.spacecraft.course.model.Test().setId(1L);
        var question = new com.harborspace.spacecraft.question.model.Question().setId(1L);
        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        var question2 = new com.harborspace.spacecraft.question.model.Question().setId(2L);
        Mockito.when(questionRepository.findById(2L)).thenReturn(Optional.of(question2));
        Mockito.when(questionRepository.countAllByIdIn(any())).thenReturn(2);
        var tqDTO1 = new TestQuestionDto().setId(1L).setQuestionId(1L).setTestId(1L).setWeight(4);
        var tqDTO2 = new TestQuestionDto().setId(2L).setQuestionId(2L).setTestId(1L).setWeight(3);
        var tqRequest1 = new TestQuestionRequest().setTestQuestionDtos(List.of(tqDTO1, tqDTO2)).setTestId(1L);
        Mockito.when(testRepository.existsById(any())).thenReturn(true);
        Mockito.when(questionRepository.countAllByIdIn(any())).thenReturn(2);
        Mockito.when(testQuestionRepository.saveAll(any())).thenReturn(List.of(new TestQuestion().setId(1L)
                .setQuestion(question).setQuestionId(1L).setTest(test).setPosition(0).setWeight(4), new TestQuestion().setId(2L)
                .setQuestion(question2).setQuestionId(2L).setTest(test).setPosition(1).setWeight(3)));

        //when
        TestQuestionResponse tqResponse = testQuestionService.save(tqRequest1);
        //then
        assertThat(tqResponse).isNotNull();
        Mockito.verify(testQuestionRepository, times(1)).saveAll(any());
    }

    @Test
    public void deleteTestQuestionByIdSuccess() {
        //given
        Mockito.when(testQuestionRepository.existsById(any())).thenReturn(true, false);

        //when
        testQuestionService.deleteById(1L);
        testQuestionService.deleteById(1L);

        //then
        Mockito.verify(testQuestionRepository, times(2)).existsById(1L);
        Mockito.verify(testQuestionRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteAllByTestId() throws ResourceNotFoundException {
        //given
        com.harborspace.spacecraft.question.model.Question question = new Question().setId(1L);
        com.harborspace.spacecraft.question.model.Question question2 = new Question().setId(2L);
        com.harborspace.spacecraft.question.model.Question question3 = new Question().setId(3L);
        com.harborspace.spacecraft.course.model.Test test = new com.harborspace.spacecraft.course.model.Test().setId(1L);
        TestQuestion tq = new TestQuestion().setId(1L).setPosition(1).setQuestion(question).setTest(test);
        TestQuestion tq2 = new TestQuestion().setId(2L).setPosition(2).setQuestion(question2).setTest(test);
        TestQuestion tq3 = new TestQuestion().setId(3L).setPosition(3).setQuestion(question3).setTest(test);

        Mockito.when(testRepository.existsById(1L)).thenReturn(true);

        //when
        testQuestionService.deleteAllByTestId(1L);
        testQuestionService.deleteAllByTestId(1L);

        //then
        Mockito.verify(testQuestionRepository, times(2)).deleteAllByTestId(1L);
    }

}
