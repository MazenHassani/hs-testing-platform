package com.harborspace.spacecraft.service;

import com.harborspace.spacecraft.question.dto.AnswerDto;
import com.harborspace.spacecraft.question.dto.ChoiceDto;
import com.harborspace.spacecraft.question.model.Answer;
import com.harborspace.spacecraft.question.model.Choice;
import com.harborspace.spacecraft.question.repository.AnswerRepository;
import com.harborspace.spacecraft.question.service.impl.AnswerServiceImpl;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class AnswerServiceTest {

    @Mock
    AnswerRepository answerRepository;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    AnswerServiceImpl answerService;

    @Test
    public void createAnswerSuccess() {
        //given
        AnswerDto answerDto = new AnswerDto().setId(1L);
        Answer answer = new Answer().setId(1L);

        Choice c1 = new Choice().setId(1L).setStatement("opt1");
        Choice c2 = new Choice().setId(2L).setStatement("opt2");
        ChoiceDto c1Dto = new ChoiceDto().setId(1L).setStatement("opt1");
        ChoiceDto c2Dto = new ChoiceDto().setId(2L).setStatement("opt2");
        List<Choice> choices = List.of(c1, c2);
        List<ChoiceDto> choicesDto = List.of(c1Dto, c2Dto);

        AnswerDto answerDto2 = new AnswerDto().setId(2L).setChoices(choicesDto);
        Answer answer2 = new Answer().setId(2L).setChoices(choices);

        Mockito.when(answerRepository.save(answer)).thenReturn(answer);
        Mockito.when(modelMapper.map(answerDto, Answer.class)).thenReturn(answer);
        Mockito.when(modelMapper.map(answer, AnswerDto.class)).thenReturn(answerDto);
        Mockito.when(answerRepository.save(answer2)).thenReturn(answer2);
        Mockito.when(modelMapper.map(answerDto2, Answer.class)).thenReturn(answer2);
        Mockito.when(modelMapper.map(answer2, AnswerDto.class)).thenReturn(answerDto2);

        //when
        AnswerDto response = answerService.create(answerDto);
        AnswerDto response2 = answerService.create(answerDto2);

        //then
        Mockito.verify(answerRepository, times(1)).save(answer);
        Mockito.verify(modelMapper, times(1)).map(answer, AnswerDto.class);
        Mockito.verify(modelMapper, times(1)).map(answerDto, Answer.class);
        Mockito.verify(answerRepository, times(1)).save(answer2);
        Mockito.verify(modelMapper, times(1)).map(answer2, AnswerDto.class);
        Mockito.verify(modelMapper, times(1)).map(answerDto2, Answer.class);
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(answerDto);
        assertThat(response.getChoices()).isNull();
        assertThat(response2).isNotNull();
        assertThat(response2).isEqualTo(answerDto2);
        assertThat(response2.getChoices()).isEqualTo(choicesDto);
    }


    @Test
    public void findOneAnswerSuccess() throws ResourceNotFoundException {
        //given
        AnswerDto answerDto = new AnswerDto().setId(1L);
        Answer answer = new Answer().setId(1L);
        Mockito.when(answerRepository.findById(2L)).thenReturn(Optional.of(answer));
        Mockito.when(modelMapper.map(answer, AnswerDto.class)).thenReturn(answerDto);

        //when
        AnswerDto response = answerService.findOne(2L);

        //then
        Mockito.verify(answerRepository, times(1)).findById(2L);
        Mockito.verify(modelMapper, times(1)).map(answer, AnswerDto.class);
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(answerDto);
    }

    @Test
    public void findOneAnswerFail() {
        //given
        Answer answer = new Answer().setId(1L);
        Mockito.when(answerRepository.findById(2L)).thenReturn(Optional.empty());

        //when
        assertThrows(ResourceNotFoundException.class, () -> answerService.findOne(2L));

        //then
        Mockito.verify(answerRepository, times(1)).findById(2L);
        Mockito.verify(modelMapper, times(0)).map(answer, AnswerDto.class);
    }

    @Test
    public void updateAnswerSuccess() throws ResourceNotFoundException {
        //given
        AnswerDto answerDto = new AnswerDto().setId(1L);
        Answer answer = new Answer().setId(1L);


        Mockito.when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));
        Mockito.when(answerRepository.save(answer)).thenReturn(answer);
        Mockito.when(modelMapper.map(answer, AnswerDto.class)).thenReturn(answerDto);

        //when
        AnswerDto response = answerService.update(answerDto);

        //then
        Mockito.verify(answerRepository, times(1)).findById(1L);
        Mockito.verify(answerRepository, times(1)).save(answer);
        Mockito.verify(modelMapper, times(1)).map(answer, AnswerDto.class);
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(answerDto);
    }

    @Test
    public void updateAnswerFail() throws ResourceNotFoundException {
        //given
        AnswerDto answerDto = new AnswerDto().setId(1L);
        Answer answer = new Answer().setId(1L);

        Mockito.when(answerRepository.findById(1L)).thenReturn(Optional.empty());

        //when
        assertThrows(ResourceNotFoundException.class, () -> answerService.update(answerDto));

        //then
        Mockito.verify(answerRepository, times(1)).findById(1L);
        Mockito.verify(answerRepository, times(0)).save(answer);
        Mockito.verify(modelMapper, times(0)).map(answer, AnswerDto.class);
    }

    @Test
    public void deleteAnswer() {
        //given
        Answer answer = new Answer().setId(1L);

        Mockito.when(answerRepository.existsById(1L)).thenReturn(true);
        Mockito.when(answerRepository.existsById(2L)).thenReturn(false);

        //when
        answerService.deleteById(1L);
        answerService.deleteById(2L);

        //then
        Mockito.verify(answerRepository, times(1)).existsById(1L);
        Mockito.verify(answerRepository, times(1)).existsById(2L);
        Mockito.verify(answerRepository, times(1)).deleteById(1L);
        Mockito.verify(answerRepository, times(0)).deleteById(2L);
    }


}
