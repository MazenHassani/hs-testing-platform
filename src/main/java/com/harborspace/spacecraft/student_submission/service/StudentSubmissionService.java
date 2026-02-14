package com.harborspace.spacecraft.student_submission.service;

import java.util.List;

import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.student_submission.dto.StudentSubmissionDto;

public interface StudentSubmissionService {
    StudentSubmissionDto create(StudentSubmissionDto studentSubmissionDto) throws ResourceNotFoundException;
    List<StudentSubmissionDto> getStudentSubmission(long taskId);
    List<StudentSubmissionDto> getAllTestSubmissions(long testId) throws ResourceNotFoundException;
}
