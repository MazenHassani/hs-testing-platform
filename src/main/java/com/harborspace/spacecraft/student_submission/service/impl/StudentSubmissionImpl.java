package com.harborspace.spacecraft.student_submission.service.impl;

import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.student_submission.dto.StudentSubmissionDto;
import com.harborspace.spacecraft.student_submission.model.StudentSubmission;
import com.harborspace.spacecraft.student_submission.repository.StudentSubmissionRepository;
import com.harborspace.spacecraft.student_submission.service.StudentSubmissionService;
import com.harborspace.spacecraft.testquestion.model.TestQuestion;
import com.harborspace.spacecraft.testquestion.repository.TestQuestionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentSubmissionImpl implements StudentSubmissionService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StudentSubmissionRepository studentSubmissionRepository;
    
    @Autowired
    private TestQuestionRepository testQuestionRepository;
    
    @Override
    public StudentSubmissionDto create(StudentSubmissionDto studentSubmissionDto) throws ResourceNotFoundException {
        StudentSubmission studentSubmission = modelMapper.map(studentSubmissionDto, StudentSubmission.class);
        Optional<TestQuestion> taskOpt = testQuestionRepository.findById(studentSubmissionDto.getTaskId());
        if (taskOpt.isEmpty()) {
            throw new ResourceNotFoundException("Task");
        }
        TestQuestion task = taskOpt.get();
        studentSubmission.setTask(task);
        //studentSubmission.getStudentId();
        studentSubmission.setId(null);
        StudentSubmission savedStudentSubmission = studentSubmissionRepository.save(studentSubmission);
        return modelMapper.map(savedStudentSubmission, StudentSubmissionDto.class);
    }
    @Override
    @Transactional
    public List<StudentSubmissionDto> getStudentSubmission(long taskId) {
        List<StudentSubmissionDto> studentSubmissions = studentSubmissionRepository.findAllByTaskId(taskId).stream().map(StudentSubmissionDto::of).collect(Collectors.toList());
        return studentSubmissions;
    }

    @Override
    public List<StudentSubmissionDto> getAllTestSubmissions(long testId) throws ResourceNotFoundException {
        List<TestQuestion> submissionsOpt = testQuestionRepository.findAllByTestId(testId);
        if (submissionsOpt.isEmpty()) {
            throw new ResourceNotFoundException("submissions");
		}
		List<StudentSubmissionDto> newList = new ArrayList<>();
		for (TestQuestion testQuestion : submissionsOpt) {
            //List<StudentSubmission> studentSubmissions = studentSubmissionRepository.findAllByTaskId(testQuestion.getId());
            List<StudentSubmissionDto> studentSubmissions = studentSubmissionRepository.findAllByTaskId(testQuestion.getId()).stream().map(StudentSubmissionDto::of).collect(Collectors.toList());
			newList.addAll(studentSubmissions);
        }
        return newList;
    }
}