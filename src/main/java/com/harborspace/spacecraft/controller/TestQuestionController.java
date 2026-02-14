package com.harborspace.spacecraft.controller;

import com.harborspace.spacecraft.ApiHelper;
import com.harborspace.spacecraft.exception.DuplicateQuestionException;
import com.harborspace.spacecraft.exception.InvalidRequestException;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.exception.TestHasnotStartedException;
import com.harborspace.spacecraft.question.dto.AnswerDto;
import com.harborspace.spacecraft.question.dto.ChoiceDto;
import com.harborspace.spacecraft.question.dto.NumberDto;
import com.harborspace.spacecraft.question.dto.QuestionDto;
import com.harborspace.spacecraft.testquestion.dto.request.TestQuestionRequest;
import com.harborspace.spacecraft.testquestion.dto.response.TestQuestionResponse;
import com.harborspace.spacecraft.testquestion.service.TestQuestionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(ApiHelper.API_TEST_QUESTION_PREFIX)
@Validated
public class TestQuestionController {

    @Autowired
    TestQuestionService testQuestionService;

    @GetMapping("/course/{course_id}/test/{test_id}")
    @PreAuthorize("hasRole('course_manager') or @securityService.isTeacher(#courseId, authentication)")
    public ResponseEntity<TestQuestionResponse> findAll(@PathVariable("test_id") Long testId, @PathVariable(
            "course_id") Long courseId) throws ResourceNotFoundException, TestHasnotStartedException {
        return new ResponseEntity<>(testQuestionService.findAllByTest(testId, false), HttpStatus.OK);
    }
//127.0.0.1//course/1/test/7/tasks
    @GetMapping("/course/{course_id}/test/{test_id}/tasks")
    //@PreAuthorize("hasRole('course_manager') or @securityService.isParticipant(#courseId, authentication)")
    public ResponseEntity<TestQuestionResponse> findAllForStudents(@PathVariable("test_id") Long testId, @PathVariable(
            "course_id") Long courseId) throws ResourceNotFoundException, TestHasnotStartedException {
        TestQuestionResponse testQuestionResponse = testQuestionService.findAllByTest(testId, true);
        sanitizeForStudent(testQuestionResponse);
        return new ResponseEntity<>(testQuestionResponse, HttpStatus.OK);
    }

    @PostMapping("/course/{course_id}/test/{test_id}")
    @PreAuthorize("hasRole('course_manager') or @securityService.isTeacher(#courseId, authentication)")
    public ResponseEntity<TestQuestionResponse> save(@RequestBody TestQuestionRequest testQuestionRequest, @PathVariable("course_id") Long courseId) throws ResourceNotFoundException, DuplicateQuestionException, InvalidRequestException {
        return new ResponseEntity<>(testQuestionService.save(testQuestionRequest), HttpStatus.OK);
    }

    @DeleteMapping("/course/{course_id}/test/{test_id}")
    @PreAuthorize("hasRole('course_manager') or @securityService.isTeacher(#courseId, authentication)")
    public ResponseEntity delete(@PathVariable("test_id") Long testId, @PathVariable("course_id") Long courseId) throws ResourceNotFoundException {
        testQuestionService.deleteAllByTestId(testId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void sanitizeForStudent(TestQuestionResponse testQuestionResponse) {
        List<QuestionDto> questionDtoList = testQuestionResponse.getQuestionsDtos();
        questionDtoList.forEach((questionDto -> {
            AnswerDto answerDto = questionDto.getAnswer();
            if (answerDto == null)
                return;
            List<ChoiceDto> choiceDtos = answerDto.getChoices();
            if (choiceDtos != null) {
                for (ChoiceDto choiceDto : choiceDtos) {
                    if (choiceDto.getSelected() != null) {
                        choiceDto.setSelected(null);
                    }
                    if (choiceDto.getIndex() != null) {
                        choiceDto.setIndex(null);
                    }
                }
                Collections.shuffle(choiceDtos);
            }
            NumberDto numberDto = answerDto.getNumber();
            if (numberDto != null) {
                numberDto.setValue(null);
            }
        }));
        testQuestionResponse.setQuestionsDtos(questionDtoList);
    }

}
