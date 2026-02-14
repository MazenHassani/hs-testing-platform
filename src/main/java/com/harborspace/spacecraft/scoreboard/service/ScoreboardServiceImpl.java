package com.harborspace.spacecraft.scoreboard.service;

import com.harborspace.spacecraft.scoreboard.model.Submission;
import com.harborspace.spacecraft.scoreboard.model.UserQuestionScore;
import com.harborspace.spacecraft.scoreboard.repository.SubmissionRepository;
import com.harborspace.spacecraft.scoreboard.repository.UserQuestionScoreRepository;
import com.harborspace.spacecraft.scoreboard.request.SubmissionRequest;
import com.harborspace.spacecraft.scoreboard.response.ScoreboardResponse;
import com.harborspace.spacecraft.scoreboard.response.UserInfoResponse;
import com.harborspace.spacecraft.scoreboard.response.UserQuestionScoreResponse;
import com.harborspace.spacecraft.testquestion.repository.TestQuestionRepository;
import com.harborspace.spacecraft.testquestion.dto.TestQuestionDto;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoreboardServiceImpl implements ScoreboardService {
    private final UserQuestionScoreRepository questionScoreRepository;
    private final SubmissionRepository submissionRepository;
    private final TestQuestionRepository testQuestionRepository;
    //private final ModelMapper modelMapper;

    private Submission createOrUpdateSubmission(SubmissionRequest request, Submission submission) {
        if (submission == null) {
            log.info("Is empty Submission");
            submission = new Submission();
            submission.setUserId(request.getUserId());
            submission.setTestQuestionId(request.getTestQuestionId());
            submission.setCreation(Instant.now());
        }

        if (StringUtils.isEmpty(request.getUserId()) || !testQuestionRepository.existsById(request.getTestQuestionId())) {
            log.error("Is empty UserId or not found TestQuestion (user id={}, testQuestion id ={})", request.getUserId(), request.getTestQuestionId());
            return null;
        }

        submission.setSubmissionApiId(request.getSubmissionApiId());
        submission.setIsAccepted(request.isAccepted());
        submission.setScore(request.getScore());
        if (!StringUtils.isEmpty(request.getSubmissionJson())) {
            submission.setSubmissionJson(request.getSubmissionJson());
        }
        submission.setStatus(request.getStatus());
        submission.setUpdate(Instant.now());
        return submissionRepository.save(submission);
    }

    private void createOrUpdateStudentQuestionScore(SubmissionRequest request, Submission submission) {
        if (submission == null) {
            log.error("Is empty Submission");
            return;
        }

        if (request == null) {
            log.error("Is empty SubmissionRequest");
            return;
        }

        var studentId = request.getUserId();
        var testQuestionId = request.getTestQuestionId();
        if (StringUtils.isEmpty(studentId)) {
            log.error("Is not valid SubmissionRequest ({})", request);
            return;
        }

        UserQuestionScore questionScore;
        var scoreboardOpt = questionScoreRepository.findByUserIdAndTestQuestionId(studentId, testQuestionId);
        if (scoreboardOpt.isPresent()) {
            questionScore = scoreboardOpt.get();
            submissionRepository
                    .findTopByUserIdAndTestQuestionIdOrderByIsAcceptedDescScoreDesc(studentId, testQuestionId)
                    .ifPresent(questionScore::setBestSubmission);
        } else {
            questionScore = new UserQuestionScore();
            questionScore.setUserId(studentId);
            questionScore.setBestSubmission(submission);
        }
        checkTestQuestion(request.getTestQuestionId(), questionScore);
        questionScoreRepository.save(questionScore);
    }

    private void checkTestQuestion(long testQuestionId, UserQuestionScore questionScore) {
        if (questionScore == null) {
            log.error("Is empty questionScore");
            return;
        }

        if (questionScore.getTestQuestion() != null && questionScore.getTestQuestion().getId().equals(testQuestionId)) {
            return;
        }

        testQuestionRepository
                .findById(testQuestionId)
                .ifPresentOrElse(questionScore::setTestQuestion, () -> log.error(" Is not found TestQuestion by id {}", testQuestionId));
    }

    private Submission createOrUpdate(SubmissionRequest request, Submission submission) {
        submission = createOrUpdateSubmission(request, submission);
        createOrUpdateStudentQuestionScore(request, submission);
        return submission;
    }

    @Override
    @Transactional
    public Submission save(SubmissionRequest request) {
        Optional<Submission> submissionOpt = submissionRepository.findBySubmissionApiId(request.getSubmissionApiId());
        return (submissionOpt.isPresent()? createOrUpdate(request, submissionOpt.get()) : createOrUpdate(request, null));
    }

    @Override
    @Transactional
    public UserQuestionScoreResponse getQuestionScore(String userId, long testQuestionId) {
        var scoreboardOpt = questionScoreRepository.findByUserIdAndTestQuestionId(userId, testQuestionId);
        UserQuestionScoreResponse response = new UserQuestionScoreResponse();
        scoreboardOpt.ifPresent(response::of);
        return response;
    }

    @Override
    @Transactional
    public ScoreboardResponse getScoreboard(long testId) {
        ScoreboardResponse scoreboardResponse = new ScoreboardResponse();
        List<TestQuestionDto> testQuestions = testQuestionRepository.findAllByTestId(testId).stream().map(TestQuestionDto::of).collect(Collectors.toList());
        scoreboardResponse.setTestQuestions(testQuestions);
        List<UserQuestionScore> scores = questionScoreRepository.findByTestQuestionTestId(testId);
        scores.forEach(studentQuestionScore -> {
            UserInfoResponse userInfoResponse;
            var studentResponseOpt = scoreboardResponse.getUserInformation()
                    .stream()
                    .filter(s -> studentQuestionScore.getUserId().equals(s.getUserId()))
                    .findFirst();
            if (studentResponseOpt.isEmpty()) {
                userInfoResponse = new UserInfoResponse();
                userInfoResponse.setUserId(studentQuestionScore.getUserId());
                scoreboardResponse.getUserInformation().add(userInfoResponse);
            } else {
                userInfoResponse = studentResponseOpt.get();
            }
            var studentQuestionScoreResponse = new UserQuestionScoreResponse();
            studentQuestionScoreResponse.of(studentQuestionScore);
            userInfoResponse.getScoreResponses().add(studentQuestionScoreResponse);

        });
        return scoreboardResponse;
    }
}
