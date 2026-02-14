package com.harborspace.spacecraft.testquestion.service;

import com.harborspace.spacecraft.exception.DuplicateQuestionException;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.exception.TestHasnotStartedException;
import com.harborspace.spacecraft.testquestion.dto.request.TestQuestionRequest;
import com.harborspace.spacecraft.testquestion.dto.response.TestQuestionResponse;

public interface TestQuestionService {
    TestQuestionResponse findAllByTest(long testId, boolean student) throws ResourceNotFoundException,
            TestHasnotStartedException;

    void deleteById(long id);
    void deleteAllByTestId(long testId) throws ResourceNotFoundException;

    TestQuestionResponse save(TestQuestionRequest testQuestionRequest) throws ResourceNotFoundException, DuplicateQuestionException;
}
