package com.harborspace.spacecraft.scoreboard.repository;

import com.harborspace.spacecraft.scoreboard.model.UserQuestionScore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserQuestionScoreRepository extends CrudRepository<UserQuestionScore, Long> {
    Optional<UserQuestionScore> findByUserIdAndTestQuestionId(String userId, Long testQuestionId);

    List<UserQuestionScore> findByTestQuestionId(Long testQuestionId);
    List<UserQuestionScore> findByTestQuestionTestId(Long testId);
}
