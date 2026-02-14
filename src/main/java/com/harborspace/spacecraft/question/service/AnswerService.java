package com.harborspace.spacecraft.question.service;

import com.harborspace.spacecraft.question.dto.AnswerDto;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;

public interface AnswerService {
    AnswerDto findOne(Long id) throws ResourceNotFoundException;

    AnswerDto create(AnswerDto answerDto);

    AnswerDto update(AnswerDto answerDto) throws ResourceNotFoundException;

    void deleteById(Long id);
}
