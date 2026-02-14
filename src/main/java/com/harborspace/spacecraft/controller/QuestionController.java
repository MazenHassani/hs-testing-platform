package com.harborspace.spacecraft.controller;

import com.harborspace.spacecraft.ApiHelper;
import com.harborspace.spacecraft.core.UserFilter;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.question.dto.QuestionDto;
import com.harborspace.spacecraft.question.service.AnswerService;
import com.harborspace.spacecraft.question.service.QuestionService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiHelper.API_QUESTION_PREFIX)
@Validated
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    AnswerService answerService;

    @GetMapping("/{id}")
    @PreAuthorize("@securityService.isQuestionCreator(#id, authentication)")
    public ResponseEntity<QuestionDto>  findOne(@PathVariable("id") long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(questionService.findOne(id), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<QuestionDto>> findAll(
            @RequestAttribute(UserFilter.USER_ID) String userId) {
        return new ResponseEntity<>(questionService.findAll(userId), HttpStatus.OK);
    }

    @PostMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<QuestionDto> createQuestion(@Valid @RequestBody QuestionDto questionDto) {
        QuestionDto questionCreated = questionService.create(questionDto);
        questionCreated.setAnswer(answerService.create(questionDto.getAnswer()));
        return new ResponseEntity<>(questionCreated, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@securityService.isQuestionCreator(#id, authentication)")
    public ResponseEntity<QuestionDto> updateQuestion(@PathVariable("id") long id,
            @Valid @RequestBody QuestionDto questionDto) throws ResourceNotFoundException {
        questionDto.setId(id);
        QuestionDto updatedQuestion = questionService.update(questionDto);
        updatedQuestion.setAnswer(answerService.update(questionDto.getAnswer()));
        return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@securityService.isQuestionCreator(#id, authentication)")
    public ResponseEntity deleteQuestion(@PathVariable("id") long id) {
        questionService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
