package com.harborspace.spacecraft.student_submission.repository;

import java.util.List;

import com.harborspace.spacecraft.student_submission.model.StudentSubmission;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentSubmissionRepository extends CrudRepository<StudentSubmission, Long> {
    Page<StudentSubmission> findAll(Pageable pageRequest);
    List<StudentSubmission> findAllByTaskId(Long taskId);
}
