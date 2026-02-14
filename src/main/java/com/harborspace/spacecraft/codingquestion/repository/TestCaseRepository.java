package com.harborspace.spacecraft.codingquestion.repository;

import com.harborspace.spacecraft.codingquestion.model.TestCase;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface TestCaseRepository extends CrudRepository<TestCase, Integer> {

    Optional<TestCase> findByIdAndCodingQuestionId(Integer id, Integer codingQuestionId);
    boolean existsByIdAndCodingQuestionId(Integer id, Integer codingQuestionId);
    void deleteByIdAndCodingQuestionId(Integer id, Integer codingQuestionId);
}
