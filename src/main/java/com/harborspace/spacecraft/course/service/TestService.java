package com.harborspace.spacecraft.course.service;

import java.util.List;

import com.harborspace.spacecraft.course.dto.TestDto;
import com.harborspace.spacecraft.exception.AlreadyStartedException;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.student_submission.StudentScore;

import org.springframework.data.domain.Page;

public interface TestService {
    TestDto findOne(Long courseId, Long id) throws ResourceNotFoundException;

    Page<TestDto> findAllByCourse(Long courseId, int page, int pageSize);

    Page<TestDto> findAll(int page, int pageSize);

    TestDto create(Long courseId, TestDto testDto) throws ResourceNotFoundException;

    TestDto update(TestDto testDto) throws ResourceNotFoundException;

    void deleteById(Long id) throws AlreadyStartedException;

    List <StudentScore> getScores(Long courseId, Long id) throws ResourceNotFoundException;

    StudentScore getScoresByStudentId(Long courseId, Long testId, String studentId) throws ResourceNotFoundException;
}
