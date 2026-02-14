package com.harborspace.spacecraft.testquestion.service.impl;

import com.harborspace.spacecraft.course.model.Test;
import com.harborspace.spacecraft.course.repository.TestRepository;
import com.harborspace.spacecraft.exception.DuplicateQuestionException;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.exception.TestHasnotStartedException;
import com.harborspace.spacecraft.question.dto.QuestionDto;
import com.harborspace.spacecraft.question.repository.QuestionRepository;
import com.harborspace.spacecraft.testquestion.dto.TestQuestionDto;
import com.harborspace.spacecraft.testquestion.model.TestQuestion;
import com.harborspace.spacecraft.testquestion.repository.TestQuestionRepository;
import com.harborspace.spacecraft.testquestion.dto.request.TestQuestionRequest;
import com.harborspace.spacecraft.testquestion.dto.response.TestQuestionResponse;
import com.harborspace.spacecraft.testquestion.service.TestQuestionService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestQuestionServiceImpl implements TestQuestionService {
    @Autowired
    private TestQuestionRepository testQuestionRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TestQuestionResponse findAllByTest(long testId, boolean student) throws ResourceNotFoundException, TestHasnotStartedException {
        Optional<Test> testOpt = testRepository.findById(testId);
        if (testOpt.isEmpty()) {
            throw new ResourceNotFoundException("Test " + testId);
        }
        Test test = testOpt.get();
        if (student && test.getStartDate().after(new Date())) {
            throw new TestHasnotStartedException(testId);
        }
        List<TestQuestion> testQuestions = testQuestionRepository.findAllByTestId(testId);
        testQuestions = testQuestions.stream().sorted(Comparator.comparingInt(TestQuestion::getPosition)).collect(Collectors.toList());
        List<QuestionDto> questionDtos = testQuestions.stream().map(TestQuestion::getQuestion).map(question -> modelMapper.map(question, QuestionDto.class))
                .collect(Collectors.toList());

        List<Long> testQuestionIds = testQuestions.stream().map(TestQuestion::getId).collect(Collectors.toList());

        List<Integer> questionWeights = testQuestions.stream().map(TestQuestion::getWeight).collect(Collectors.toList());

        return new TestQuestionResponse()
                .setTestQuestionIds(testQuestionIds)
                .setQuestionsDtos(questionDtos)
                .setQuestionsWeights(questionWeights)
                .setTestId(testId);
    }

    @Override
    public void deleteById(long id) {
        if (testQuestionRepository.existsById(id)) {
            testQuestionRepository.deleteById(id);
        }
    }

    @Override
    @Transactional
    public void deleteAllByTestId(long testId) throws ResourceNotFoundException {
        if (!testRepository.existsById(testId)) {
            throw new ResourceNotFoundException("Test " + testId);
        }
        testQuestionRepository.deleteAllByTestId(testId);
    }

    @Override
    public TestQuestionResponse save(TestQuestionRequest testQuestionRequest) throws ResourceNotFoundException,
            DuplicateQuestionException {
        Long testId = testQuestionRequest.getTestId();
        List<Long> questionsIds = testQuestionRequest.getTestQuestionDtos().stream().map(TestQuestionDto::getQuestionId).collect(Collectors.toList());
        if (!testRepository.existsById(testId)) {
            throw new ResourceNotFoundException("Test " + testId);
        }

        HashSet<Long> uniqueQustionsIds = new HashSet<>(questionsIds);
        if (uniqueQustionsIds.size() != questionsIds.size()) {
            throw new DuplicateQuestionException();
        }

        if (questionRepository.countAllByIdIn(questionsIds) !=
                questionsIds.size()) {
            throw new ResourceNotFoundException("Question ");
        }

        List<TestQuestion> testQuestionsToSave = new ArrayList<>();

        for (int i = 0; i < testQuestionRequest.getTestQuestionDtos().size(); i++) {
            var tqDTO = testQuestionRequest.getTestQuestionDtos().get(i);
            TestQuestion tq = new TestQuestion().setId(tqDTO.getId());
            tq.setTestId(testId);
            tq.setQuestionId(questionsIds.get(i));
            tq.setWeight(testQuestionRequest.getTestQuestionDtos().get(i).getWeight());
            tq.setPosition(i);

            testQuestionsToSave.add(tq);
        }

        var idsToSave = testQuestionRequest.getTestQuestionDtos().stream().map(TestQuestionDto::getId).filter(Objects::nonNull).collect(Collectors.toList());
        testQuestionRepository.deleteAllByTestIdAndIdNotIn(testId, idsToSave);

        List<TestQuestion> savedTestQuestions = (List<TestQuestion>) testQuestionRepository.saveAll(testQuestionsToSave);
        List<Long> testQuestionIds = new ArrayList<>();
        List<QuestionDto> questionsDtos = new ArrayList<>();
        List<Integer> questionsWeights = new ArrayList<>();
        TestQuestionResponse response = new TestQuestionResponse();

        for (TestQuestion tq : savedTestQuestions) {
            questionsDtos.add(modelMapper.map(questionRepository.findById(tq.getQuestionId()).get(), QuestionDto.class));
            questionsWeights.add(tq.getWeight());
            testQuestionIds.add(tq.getId());
        }
        response.setQuestionsWeights(questionsWeights);
        response.setQuestionsDtos(questionsDtos);
        response.setTestId(testId);
        response.setTestQuestionIds(testQuestionIds);
        return response;
    }
}
