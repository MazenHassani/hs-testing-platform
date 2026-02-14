package com.harborspace.spacecraft.question.repository;

import com.harborspace.spacecraft.question.model.Answer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Long> {
}
