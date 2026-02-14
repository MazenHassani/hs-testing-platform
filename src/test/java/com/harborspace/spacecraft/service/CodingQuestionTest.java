package com.harborspace.spacecraft.service;


import com.harborspace.spacecraft.codingquestion.dto.CodingQuestionSphere;
import com.harborspace.spacecraft.codingquestion.dto.JudgeSphere;
import com.harborspace.spacecraft.codingquestion.model.CodingQuestion;
import com.harborspace.spacecraft.codingquestion.model.Judge;
import com.harborspace.spacecraft.codingquestion.repository.CodingQuestionRepository;
import com.harborspace.spacecraft.codingquestion.repository.JudgeRepository;
import com.harborspace.spacecraft.codingquestion.repository.TestCaseRepository;
import com.harborspace.spacecraft.codingquestion.service.impl.SphereQuestionServiceImpl;
import com.harborspace.spacecraft.sphere.engine.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class CodingQuestionTest {

    @Mock
    CodingQuestionRepository codingQuestionRepository;
    @Mock
    TestCaseRepository testCaseRepository;
    @Mock
    JudgeRepository judgeRepository;

    @InjectMocks
    SphereQuestionServiceImpl codingQuestionService;

    @Test
    public void createAnswerSuccess() {
        //given
        CodingQuestionSphere codingQuestionSphere = new CodingQuestionSphere().setId(1).setName("P1");
        CodingQuestion codingQuestion = new CodingQuestion().setId(1).setName("P1");
        JudgeSphere judgeSphere = new JudgeSphere().setCompilerId(1);
        Judge judge = new Judge().setCompilerId(1);
//        Mockito.when(codingQuestionRepository.save(codingQuestion)).thenReturn(codingQuestion);
//        Mockito.when(judgeRepository.save(judge)).thenReturn(judge);
//
//        //when
//        assertThrows(NotFoundException.class, ()->codingQuestionService.create(codingQuestionSphere, judgeSphere));
//
//        //then
//        Mockito.verify(codingQuestionRepository, times(1)).save(codingQuestion);
//        Mockito.verify(judgeRepository, times(1)).save(judge);
    }
}
