package com.harborspace.spacecraft.scoreboard.repository;

import com.harborspace.spacecraft.scoreboard.model.Submission;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends CrudRepository<Submission, Long> {

    Optional<Submission> findTopByUserIdAndTestQuestionIdOrderByIsAcceptedDescScoreDesc(String userId, Long testQuestionId);

    Optional<Submission> findBySubmissionApiId(Long submissionApiId);


    Optional<Submission> findOptionalByUserIdAndTestQuestionId(String userId, Long testQuestionId);

    @Query(value = "select * from submission sub where sub.status<10  and sub.creation >=:startDate and sub.creation <= :endDate  limit 10",
            nativeQuery = true)
    List<Submission> findRequiringVerification(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

}
