package com.harborspace.spacecraft.codingquestion.service;

import com.harborspace.spacecraft.codingquestion.dto.CodingQuestionSphere;
import com.harborspace.spacecraft.codingquestion.dto.JudgeSphere;
import com.harborspace.spacecraft.codingquestion.dto.request.CodingQuestionRequest;
import com.harborspace.spacecraft.codingquestion.dto.TestCaseSphere;
import com.harborspace.spacecraft.codingquestion.model.Judge;
import com.harborspace.spacecraft.codingquestion.dto.response.JudgeResponse;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.sphere.engine.exception.SphereClientException;

public interface SphereQuestionService {

    CodingQuestionSphere findOne(Integer id) throws ResourceNotFoundException;

    JudgeResponse findOneJudge(Integer id) throws ResourceNotFoundException;

    TestCaseSphere findOneTestCase(Integer id, Integer number) throws ResourceNotFoundException;

    CodingQuestionSphere create(CodingQuestionRequest codingQuestionRequest);

    TestCaseSphere addTestCase(Integer id, TestCaseSphere testCaseDto) throws ResourceNotFoundException;

    Judge createJudge(JudgeSphere judge) throws SphereClientException;

    CodingQuestionSphere update(Integer id, CodingQuestionRequest codingQuestionRequest) throws ResourceNotFoundException;

    Judge updateJudge(Integer id, JudgeSphere judgeSphere) throws SphereClientException, ResourceNotFoundException;

    TestCaseSphere updateTestCase(Integer id, Integer number, TestCaseSphere testCaseDto) throws ResourceNotFoundException;

    void delete(Integer id);

    void deleteTestCase(Integer id, Integer number);
}
