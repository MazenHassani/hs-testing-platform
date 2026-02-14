package com.harborspace.spacecraft.service;

import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.question.dto.QuestionDto;
import com.harborspace.spacecraft.question.model.Question;
import com.harborspace.spacecraft.question.repository.QuestionRepository;
import com.harborspace.spacecraft.question.service.impl.QuestionServiceImpl;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private QuestionServiceImpl questionService;

    @Test
    void testFindOneQuestionSuccess() throws ResourceNotFoundException {
        //given
        Question question = new Question();
        question.setTitle("Question 1");
        question.setId(1L).setQuestionType(Question.QuestionType.SPHERE);

        QuestionDto questionDto = new QuestionDto();
        questionDto.setTitle("Question 1");
        questionDto.setId(1L).setQuestionType(Question.QuestionType.SPHERE);

        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        Mockito.when(modelMapper.map(question, QuestionDto.class)).thenReturn(questionDto);

        //when
        questionService.findOne(1L);

        //then
        Mockito.verify(questionRepository, times(1)).findById(1L);
        Mockito.verify(modelMapper, times(1)).map(question, QuestionDto.class);

    }

    @Test
    void testFindOneQuestionFails() throws ResourceNotFoundException {
        //given
        Question question = new Question();
        question.setTitle("Question 1");
        question.setId(1L).setQuestionType(Question.QuestionType.SPHERE);

        Mockito.when(questionRepository.findById(2L)).thenReturn(Optional.empty());

        //when
        assertThrows(ResourceNotFoundException.class, () -> questionService.findOne(2L));


        //then
        Mockito.verify(questionRepository, times(1)).findById(2L);
        Mockito.verify(modelMapper, times(0)).map(Mockito.any(), Mockito.any());
    }

    @Test
    void testFindAllSuccess() throws ResourceNotFoundException {
        //given
        String userId = "1";
        Question question = new Question();
        question.setTitle("Question 1");
        question.setId(1L).setQuestionType(Question.QuestionType.SPHERE);
        Question question2 = new Question();
        question.setTitle("Question 2");
        question.setId(2L).setQuestionType(Question.QuestionType.SPHERE);

        QuestionDto questionDto = new QuestionDto();
        questionDto.setTitle("Question 1");
        questionDto.setId(1L).setQuestionType(Question.QuestionType.SPHERE);
        QuestionDto questionDto2 = new QuestionDto();
        questionDto.setTitle("Question 2");
        questionDto.setId(2L).setQuestionType(Question.QuestionType.SPHERE);

        Mockito.when(questionRepository.findAllByCreator(userId)).thenReturn(List.of(question, question2));
        Mockito.when(modelMapper.map(question, QuestionDto.class)).thenReturn(questionDto);
        Mockito.when(modelMapper.map(question2, QuestionDto.class)).thenReturn(questionDto2);

        //when
        questionService.findAll(userId);

        //then
        Mockito.verify(questionRepository, times(1)).findAllByCreator(userId);
        Mockito.verify(modelMapper, times(1)).map(question, QuestionDto.class);
        Mockito.verify(modelMapper, times(1)).map(question2, QuestionDto.class);

    }

    @Test
    void createQuestionSuccess() {
        //when
        Question question = new Question();
        question.setTitle("Question 1");
        question.setId(1L).setQuestionType(Question.QuestionType.SPHERE);

        Question question_req = new Question();
        question_req.setTitle("Question 1");
        question_req.setId(null).setQuestionType(Question.QuestionType.SPHERE);


        QuestionDto questionDto = new QuestionDto();
        questionDto.setTitle("Question 1").setQuestionType(Question.QuestionType.SPHERE);

        QuestionDto questionDto_res = new QuestionDto();
        questionDto_res.setTitle("Question 1");
        questionDto_res.setId(1L).setQuestionType(Question.QuestionType.SPHERE);

        Mockito.when(questionRepository.save(question_req)).thenReturn(question);
        Mockito.when(modelMapper.map(questionDto, Question.class)).thenReturn(question_req);
        Mockito.when(modelMapper.map(question, QuestionDto.class)).thenReturn(questionDto_res);

        //when
        assertThat(questionService.create(questionDto)).isEqualTo(questionDto_res);

        //then
        Mockito.verify(questionRepository, times(1)).save(question_req);
        Mockito.verify(modelMapper, times(1)).map(questionDto, Question.class);
        Mockito.verify(modelMapper, times(1)).map(question, QuestionDto.class);
    }


    @Test
    void createQuestionForcedDuplicateId() {
        //when
        Question question = new Question();
        question.setTitle("Question 1");
        question.setQuestionType(Question.QuestionType.SPHERE);
        question.setId(1L);
        Question question_req = new Question();
        question_req.setTitle("Question 1");
        question_req.setQuestionType(Question.QuestionType.SPHERE);
        question_req.setId(null);
        Question question2 = new Question();
        question2.setTitle("Question 2");
        question2.setQuestionType(Question.QuestionType.SPHERE);

        question2.setId(2L);
        Question question_req2 = new Question();
        question_req2.setTitle("Question 2");
        question_req2.setQuestionType(Question.QuestionType.SPHERE);

        question_req2.setId(null);

        QuestionDto questionDto = new QuestionDto();
        questionDto.setTitle("Question 1");
        questionDto.setQuestionType(Question.QuestionType.SPHERE);

        QuestionDto questionDto_res = new QuestionDto();
        questionDto_res.setTitle("Question 1");
        questionDto_res.setQuestionType(Question.QuestionType.SPHERE);

        questionDto_res.setId(1L);

        QuestionDto questionDto2 = new QuestionDto();
        questionDto2.setTitle("Question 2");
        questionDto2.setQuestionType(Question.QuestionType.SPHERE);

        QuestionDto questionDto2_res = new QuestionDto();
        questionDto2_res.setTitle("Question 2");
        questionDto2_res.setQuestionType(Question.QuestionType.SPHERE);

        questionDto2_res.setId(2L);

        Mockito.when(questionRepository.save(question_req)).thenReturn(question);
        Mockito.when(questionRepository.save(question_req2)).thenReturn(question2);
        Mockito.when(modelMapper.map(questionDto, Question.class)).thenReturn(question_req);
        Mockito.when(modelMapper.map(questionDto2, Question.class)).thenReturn(question_req2);
        Mockito.when(modelMapper.map(question, QuestionDto.class)).thenReturn(questionDto_res);
        Mockito.when(modelMapper.map(question2, QuestionDto.class)).thenReturn(questionDto2_res);

        //when
        assertThat(questionService.create(questionDto)).isEqualTo(questionDto_res);
        questionDto2.setId(1L);
        assertThat(questionService.create(questionDto2)).isEqualTo(questionDto2_res);
        questionDto2.setId(null);

        //then
        Mockito.verify(questionRepository, times(1)).save(question_req);
        Mockito.verify(questionRepository, times(1)).save(question_req2);
        Mockito.verify(modelMapper, times(1)).map(questionDto, Question.class);
        Mockito.verify(modelMapper, times(1)).map(question, QuestionDto.class);
        Mockito.verify(modelMapper, times(1)).map(questionDto2, Question.class);
        Mockito.verify(modelMapper, times(1)).map(question2, QuestionDto.class);
    }

    @Test
    void updateQuestionSuccess() throws ResourceNotFoundException {
        Question question = new Question();
        question.setTitle("Question 1");
        question.setQuestionType(Question.QuestionType.SPHERE);
        ;
        question.setId(1L);

        QuestionDto updateQuestion = new QuestionDto();
        updateQuestion.setTitle("Question 1");
        updateQuestion.setQuestionType(Question.QuestionType.SPHERE);
        updateQuestion.setId(1L);


        Question updatedQuestion = question;
        BeanUtils.copyProperties(updateQuestion, question);

        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        Mockito.when(questionRepository.save(updatedQuestion)).thenReturn(updatedQuestion);
        Mockito.when(modelMapper.map(updatedQuestion, QuestionDto.class)).thenReturn(updateQuestion);

        //when
        assertThat(questionService.update(updateQuestion)).isEqualTo(updateQuestion);

        //then
        Mockito.verify(questionRepository, times(1)).findById(1L);
        Mockito.verify(questionRepository, times(1)).save(updatedQuestion);
        Mockito.verify(modelMapper, times(1)).map(updatedQuestion, QuestionDto.class);
    }

    @Test
    void updateQuestionFail() throws ResourceNotFoundException {
        QuestionDto updateQuestion = new QuestionDto();
        updateQuestion.setTitle("Question 1");
        updateQuestion.setQuestionType(Question.QuestionType.SPHERE);
        updateQuestion.setId(1L);

        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        //when
        assertThrows(ResourceNotFoundException.class, () -> questionService.update(updateQuestion));

        //then
        Mockito.verify(questionRepository, times(1)).findById(1L);
        Mockito.verify(questionRepository, times(0)).save(Mockito.any());
        Mockito.verify(modelMapper, times(0)).map(Mockito.any(), Mockito.any());
    }

    @Test
    void deleteQuestionSuccess() {
        Question question = new Question();
        question.setTitle("Question 1");
        question.setQuestionType(Question.QuestionType.SPHERE);
        ;
        question.setId(1L);

        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question), Optional.empty());

        //when
        questionService.deleteById(1L);
        questionService.deleteById(1L);


        //then
        Mockito.verify(questionRepository, times(2)).findById(1L);
        Mockito.verify(questionRepository, times(1)).deleteById(1L);
    }

}
