package com.harborspace.spacecraft.question.service;

import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.question.dto.QuestionDto;
import java.util.List;

public interface QuestionService {
    QuestionDto findOne(Long id) throws ResourceNotFoundException;

    List<QuestionDto> findAll(String userId);

    QuestionDto create(QuestionDto questionDto);

    QuestionDto update(QuestionDto questionDto) throws ResourceNotFoundException;

    void deleteById(Long id);
}
