package com.harborspace.spacecraft.service;

import com.harborspace.spacecraft.AbstractTest;
import com.harborspace.spacecraft.scoreboard.model.Submission;
import com.harborspace.spacecraft.scoreboard.request.SubmissionRequest;
import com.harborspace.spacecraft.scoreboard.service.ScoreboardService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class ScoreboardServiceTest extends AbstractTest {
    private static final int BEST_SCORE = 200;

    @Autowired
    private ScoreboardService scoreboardService;

    @BeforeEach
    public void init() {
        initDefaultObject();
    }

    @Test
    public void createOnlyOneSubmission() {
        var testQuestionId = testQuestions.get(0).getId();
        var submission = scoreboardService.save(
                SubmissionRequest.builder()
                        .submissionApiId(10L)
                        .testQuestionId(testQuestionId)
                        .userId(STUDENT_ID)
                        .build());
        assertNotNull(submission);
        assertNotNull(submission.getId());
        var submissionFromDB = submissionRepository.findById(submission.getId());
        assertTrue(submissionFromDB.isPresent());
        var submission1 = submissionFromDB.get();
        assertEquals(submission1.getTestQuestionId(), testQuestionId);
        log.info(submissionRepository.findAll().toString());
        submissions.add(submission);
    }

    @Test
    public void changeBestResult() {
        var testQuestionId = testQuestions.get(0).getId();
        Submission submission = scoreboardService.save(
                SubmissionRequest.builder()
                        .submissionApiId(10L)
                        .userId(STUDENT_ID)
                        .testQuestionId(testQuestionId)
                        .score(BEST_SCORE - 2)
                        .accepted(true)
                        .build());
        submissions.add(submission);

        var best = scoreboardService.save(
                SubmissionRequest.builder()
                        .submissionApiId(20L)
                        .userId(STUDENT_ID)
                        .testQuestionId(testQuestionId)
                        .score(BEST_SCORE)
                        .accepted(true)
                        .build());
        assertNotNull(best);
        submissions.add(best);

        submission = scoreboardService.save(
                SubmissionRequest.builder()
                        .submissionApiId(30L)
                        .userId(STUDENT_ID)
                        .testQuestionId(testQuestionId)
                        .score(BEST_SCORE - 1)
                        .accepted(true)
                        .build());
        submissions.add(submission);

        log.info(submissions.toString());
        log.info(submission.toString());
        assertNotNull(submission);
        assertEquals(testQuestionId, submission.getTestQuestionId());
        assertEquals(STUDENT_ID, submission.getUserId());
        var userQuestionScoreResponse = scoreboardService.getQuestionScore(STUDENT_ID, submission.getTestQuestionId());
        assertNotNull(userQuestionScoreResponse);
        assertEquals(userQuestionScoreResponse.getSubmissionId(), best.getId().longValue());
        assertEquals(userQuestionScoreResponse.getTestQuestionId(), testQuestionId.longValue());
        assertEquals(BEST_SCORE, userQuestionScoreResponse.getScore());
        log.info(userQuestionScoreResponse.toString());
    }

    @Test
    public void getQuestionScoreForRest() {
        changeBestResult();
        var testQuestionId = testQuestions.get(0).getId();
        var userQuestionScoreResponse = scoreboardService.getQuestionScore(STUDENT_ID, testQuestionId);
        assertNotNull(userQuestionScoreResponse);
        assertEquals(userQuestionScoreResponse.getTestQuestionId(), testQuestionId.longValue());
        assertEquals(BEST_SCORE, userQuestionScoreResponse.getScore());
    }

    @Test
    public void createScoreboardOnlyOneStudentAndTwoQuestions() {
        var question2 = initQuestion("testQuestion2", "testCreator2");
        var test2 = initTest("test2", course);
        var testQuestion2 = initTestQuestion(test2, question2);
        var test2Id = test2.getId();
        var testQuestion2Id = testQuestion2.getId();


        changeBestResult();
        var submission = scoreboardService.save(
                SubmissionRequest.builder()
                        .submissionApiId(40L)
                        .userId(STUDENT_ID)
                        .testQuestionId(testQuestion2Id)
                        .score(BEST_SCORE - 150)
                        .build());
        submissions.add(submission);
        var scoreboardResponse = scoreboardService.getScoreboard(test2Id);
        log.info(scoreboardResponse.toString());
        assertNotNull(scoreboardResponse);
        assertNotNull(scoreboardResponse.getUserInformation());
        assertFalse(CollectionUtils.isEmpty(scoreboardResponse.getUserInformation()));
    }

    @Test
    public void createScoreboardByTwoStudent() {
        createScoreboardOnlyOneStudentAndTwoQuestions();
        var userId2 = "studentId2";
        var testId = testQuestions.get(0).getTestId();
        var testQuestionId = testQuestions.get(0).getId();

        var submission = scoreboardService.save(
                SubmissionRequest.builder()
                        .submissionApiId(50L)
                        .userId(userId2)
                        .testQuestionId(testQuestionId)
                        .score(BEST_SCORE - 145)
                        .build());
        submissions.add(submission);
        var scoreboardResponse = scoreboardService.getScoreboard(testId);
        log.info(scoreboardResponse.toString());
        assertNotNull(scoreboardResponse);
        assertNotNull(scoreboardResponse.getUserInformation());
        assertFalse(CollectionUtils.isEmpty(scoreboardResponse.getUserInformation()));
        var userResponse = scoreboardResponse.getUserInformation().stream()
                .filter(u -> u.getUserId().equals(userId2))
                .findFirst();
        assertTrue(userResponse.isPresent());
        var scoreResponses = userResponse.get().getScoreResponses();
        assertNotNull(scoreResponses);
        assertTrue(scoreResponses.stream().anyMatch(r -> testQuestionId.equals(r.getTestQuestionId())));
    }

    @AfterEach
    public void clear() {
        clearTestObject();
    }

}
