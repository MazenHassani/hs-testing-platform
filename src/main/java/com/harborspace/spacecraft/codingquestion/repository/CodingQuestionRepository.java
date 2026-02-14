package com.harborspace.spacecraft.codingquestion.repository;

import com.harborspace.spacecraft.codingquestion.model.CodingQuestion;
import org.springframework.data.repository.CrudRepository;

public interface CodingQuestionRepository extends CrudRepository<CodingQuestion, Integer> {
}
