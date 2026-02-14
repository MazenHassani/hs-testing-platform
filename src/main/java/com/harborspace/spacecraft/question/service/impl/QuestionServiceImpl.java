package com.harborspace.spacecraft.question.service.impl;

import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.question.dto.QuestionDto;
import com.harborspace.spacecraft.question.model.Question;
import com.harborspace.spacecraft.question.repository.QuestionRepository;
import com.harborspace.spacecraft.question.service.QuestionService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;


    @Autowired
    private ModelMapper modelMapper;

    @Override
    public QuestionDto findOne(Long id) throws ResourceNotFoundException {
        return questionRepository.findById(id)
                .map(question -> modelMapper.map(question,
                        QuestionDto.class))
                .orElseThrow(() -> new ResourceNotFoundException("Question"));
    }

    @Override
    public List<QuestionDto> findAll(String userId) {
        List<Question> questions = questionRepository.findAllByCreator(userId);
        return questions.stream().map(question -> modelMapper.map(question, QuestionDto.class)).collect(Collectors.toList());
    }

    @Override
    public QuestionDto create(QuestionDto questionDto) {
        questionDto.setId(null);
        Question question = modelMapper.map(questionDto, Question.class);
        Question savedQuestion = questionRepository.save(question);
        return modelMapper.map(savedQuestion, QuestionDto.class);
    }

    @Override
    public QuestionDto update(QuestionDto questionDto) throws ResourceNotFoundException {
        Optional<Question> questionFound = questionRepository.findById(questionDto.getId());
        if (questionFound.isEmpty()) {
            throw new ResourceNotFoundException("Question");
        }
        Question updatedQuestion = questionFound.get();
        BeanUtils.copyProperties(questionDto, updatedQuestion);
        updatedQuestion = questionRepository.save(updatedQuestion);
        return modelMapper.map(updatedQuestion, QuestionDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Question> questionFound = questionRepository.findById(id);
        if (questionFound.isPresent()) {
            questionRepository.deleteById(id);
        }
    }
}
