package com.harborspace.spacecraft.controller;

import com.harborspace.spacecraft.ApiHelper;
import com.harborspace.spacecraft.codingquestion.dto.CodingQuestionSphere;
import com.harborspace.spacecraft.codingquestion.dto.request.CodingQuestionRequest;
import com.harborspace.spacecraft.codingquestion.dto.TestCaseSphere;
import com.harborspace.spacecraft.codingquestion.dto.response.JudgeResponse;
import com.harborspace.spacecraft.codingquestion.service.SphereQuestionService;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiHelper.API_CODE_QUESTION)
@Validated
public class CodingQuestionController {

    @Autowired
    private SphereQuestionService codingQuestionService;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CodingQuestionSphere> findOne(@PathVariable("id") Integer id) throws ResourceNotFoundException {
        return new ResponseEntity<>(codingQuestionService.findOne(id), HttpStatus.OK);
    }

    @GetMapping("/judges/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<JudgeResponse> findOneJudge(@PathVariable("id") Integer id) throws ResourceNotFoundException {
        return new ResponseEntity<>(codingQuestionService.findOneJudge(id), HttpStatus.OK);
    }

    @PostMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CodingQuestionSphere> createCodingQuestion(@Valid @RequestBody CodingQuestionRequest codingQuestionRequest) {
        return new ResponseEntity<>(codingQuestionService.create(codingQuestionRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CodingQuestionSphere> updateCodingQuestion(@PathVariable("id") Integer id,
                                                                     @Valid @RequestBody CodingQuestionRequest codingQuestionRequest) throws ResourceNotFoundException {
        return new ResponseEntity<>(codingQuestionService.update(id, codingQuestionRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity deleteCodingQuestion(@PathVariable("id") Integer id) {
        codingQuestionService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }


    @PostMapping("/{id}/testcase")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TestCaseSphere> addTestCase(@PathVariable("id") Integer id,
                                                      @Valid @RequestBody TestCaseSphere testCaseDto) throws ResourceNotFoundException {
        return new ResponseEntity<>(codingQuestionService.addTestCase(id, testCaseDto), HttpStatus.CREATED);
    }


    @PutMapping("/{id}/testcase/{number}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TestCaseSphere> updateTestCase(@PathVariable("id") Integer id,
                                                         @PathVariable("number") Integer number,
                                                         @Valid @RequestBody TestCaseSphere testCaseDto) throws ResourceNotFoundException {
        return new ResponseEntity<>(codingQuestionService.updateTestCase(id, number, testCaseDto), HttpStatus.OK);
    }


    @GetMapping("/{id}/testcase/{number}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TestCaseSphere> findOneTestCase(@PathVariable("id") Integer id, @PathVariable("number") Integer number) throws ResourceNotFoundException {
        return new ResponseEntity<>(codingQuestionService.findOneTestCase(id, number), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/testcase/{number}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity deleteTestCase(@PathVariable("id") Integer id, @PathVariable("number") Integer number) {
        codingQuestionService.deleteTestCase(id, number);
        return new ResponseEntity(HttpStatus.OK);
    }
}
