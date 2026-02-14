package com.harborspace.spacecraft.question.service.impl;

import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.question.dto.AnswerDto;
import com.harborspace.spacecraft.question.model.Answer;
import com.harborspace.spacecraft.question.repository.AnswerRepository;
import com.harborspace.spacecraft.question.service.AnswerService;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AnswerDto findOne(Long id) throws ResourceNotFoundException {
        return answerRepository.findById(id)
                .map(answer -> modelMapper.map(answer,
                        AnswerDto.class))
                .orElseThrow(() -> new ResourceNotFoundException("Answer"));
    }

    @Override
    public AnswerDto create(AnswerDto answerDto) {
        answerDto.setId(null);
        Answer answer = modelMapper.map(answerDto, Answer.class);
        Answer savedAnswer = answerRepository.save(answer);
        return modelMapper.map(savedAnswer, AnswerDto.class);
    }

    @Override
    public AnswerDto update(AnswerDto answerDto) throws ResourceNotFoundException {
        Optional<Answer> answerFound = answerRepository.findById(answerDto.getId());
        if (answerFound.isEmpty()) {
            throw new ResourceNotFoundException("Answer");
        }
        Answer updatedAnswer = answerFound.get();
        BeanUtils.copyProperties(answerDto, updatedAnswer);
        updatedAnswer = answerRepository.save(updatedAnswer);
        return modelMapper.map(updatedAnswer, AnswerDto.class);
    }

    @Override
    public void deleteById(Long id) {
        if (answerRepository.existsById(id)) {
            answerRepository.deleteById(id);
        }
    }
}
