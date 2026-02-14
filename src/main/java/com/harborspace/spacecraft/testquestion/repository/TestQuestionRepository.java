package com.harborspace.spacecraft.testquestion.repository;

import com.harborspace.spacecraft.testquestion.model.TestQuestion;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TestQuestionRepository extends CrudRepository<TestQuestion, Long> {
    @Transactional
    List<TestQuestion> findAllByTestId(Long testId);

    Optional<TestQuestion> findByQuestionIdAndTestId(Long questionId, Long testId);

    long deleteAllByTestId(Long testId);

    @Transactional
    @Modifying
    @Query("DELETE FROM test_question tq WHERE test_id = :testId and NOT id IN :testQuestionIds")
    int deleteAllByTestIdAndIdNotIn(@Param("testId") Long testId, @Param("testQuestionIds") List<Long> ids);
}