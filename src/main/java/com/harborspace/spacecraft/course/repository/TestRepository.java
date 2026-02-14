package com.harborspace.spacecraft.course.repository;

import com.harborspace.spacecraft.course.model.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends CrudRepository<Test, Long> {
    Page<Test> findAll(Pageable pageRequest);

    Page<Test> findAllByCourseId(Long courseId, Pageable pageRequest);
}