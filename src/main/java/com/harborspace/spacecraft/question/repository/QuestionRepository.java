package com.harborspace.spacecraft.question.repository;

import com.harborspace.spacecraft.question.model.Question;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {
    List<Question> findAllByCreator(String creator);
    boolean existsByIdAndCreator(long id, String creator);
    int countAllByIdIn(List<Long> ids);
}
