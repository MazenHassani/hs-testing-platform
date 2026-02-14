package com.harborspace.spacecraft.service;

import com.harborspace.spacecraft.AbstractTest;
import com.harborspace.spacecraft.scoreboard.SubmissionStatus;
import com.harborspace.spacecraft.scoreboard.request.SubmissionRequest;
import com.harborspace.spacecraft.scoreboard.service.ScoreboardService;
import com.harborspace.spacecraft.sphere.engine.request.CheckCodeProblemsRequest;
import com.harborspace.spacecraft.sphere.engine.response.SubmissionsResponse;
import com.harborspace.spacecraft.sphere.engine.sevice.SphereEngineClient;
import com.harborspace.spacecraft.task.SubmissionTask;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

;

@Slf4j
public class SubmissionTaskTest extends AbstractTest {

    @Autowired
    private SphereEngineClient sphereEngineClient;

    @Autowired
    private ScoreboardService scoreboardService;

    @Autowired
    private SubmissionTask submissionTask;

    @BeforeEach
    public void init() {
        initDefaultObject();
    }

//    @Test
    public void initSubmissionInProblems() {
        SubmissionsResponse submissionInProblems = sphereEngineClient.submissionCreate(CheckCodeProblemsRequest.builder()
                .problemId(EXSIST_PROBLEM_ID)
                .compilerId(DEFAULT_COMPILER_ID)
                .source(CODE_DEFAULT)
                .build());


        if (submissionInProblems != null) {
            var testQuestionId = testQuestions.get(0).getId();
            var submission = scoreboardService.save(SubmissionRequest.builder()
                    .submissionApiId(submissionInProblems.getId())
                    .testQuestionId(testQuestionId)
                    .userId(STUDENT_ID)
                    .status(SubmissionStatus.COMPILATION)
                    .build());
            submissions.add(submission);
            assertNotNull(submission);
            var delay = 2000;
            log.info("Need check:" + submissionRepository.findRequiringVerification(Instant.now().minus(1, ChronoUnit.DAYS), Instant.now()).toString());
            awaitAsyncProcessing(delay);
            while (submission.getStatus().getValue() < FINAL_STATUS_BOUNDARY) {
                log.info("run transferSubmission");
                submissionTask.transferSubmission();
                awaitAsyncProcessing(delay);
                submission = submissionRepository.findById(submission.getId()).get();
            }
            log.info(submission.toString());
            assertNotNull(submission);
            assertEquals(submission.getTestQuestion().getId(), testQuestionId);
            assertEquals(STUDENT_ID, submission.getUserId());
            assertTrue(submission.getStatus().getValue() > FINAL_STATUS_BOUNDARY);
            assertNotNull(submission.getCreation());
            assertNotNull(submission.getUpdate());

        }
    }

    @AfterEach
    public void clear() {
        clearTestObject();
    }

}
