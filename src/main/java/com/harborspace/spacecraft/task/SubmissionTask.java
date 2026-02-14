package com.harborspace.spacecraft.task;

import com.harborspace.spacecraft.scoreboard.SubmissionStatus;
import com.harborspace.spacecraft.scoreboard.repository.SubmissionRepository;
import com.harborspace.spacecraft.scoreboard.request.SubmissionRequest;
import com.harborspace.spacecraft.scoreboard.service.ScoreboardService;
import com.harborspace.spacecraft.sphere.engine.sevice.SphereEngineClient;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmissionTask {

    private final SubmissionRepository submissionRepository;
    private final SphereEngineClient sphereEngineClient;
    private final ScoreboardService scoreboardService;

    @Scheduled(fixedDelayString = "${submission.refresh.delay}")
    @Async
    @Transactional
    public void transferSubmission() {
        submissionRepository
                .findRequiringVerification(Instant.now().minus(1, ChronoUnit.DAYS), Instant.now())
                .forEach(submission -> {
                    var response = sphereEngineClient.submissionStatusProblems(submission.getSubmissionApiId());
                    try {
                        var status = SubmissionStatus.fromCode(response.getResult().getStatus().getCode());
                        scoreboardService.save(SubmissionRequest.builder()
                                .submissionApiId(submission.getSubmissionApiId())
                                .userId(submission.getUserId())
                                .receivedName(submission.getRecievedName())
                                .receivedEmail(submission.getRecievedEmail())
                                .testQuestionId(submission.getTestQuestionId())
                                .accepted(SubmissionStatus.SUCCESS.equals(status))
                                .score((int) response.getResult().getScore())
                                .status(status)
                                .submissionJson(response.toString())
                                .build());
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                });
    }
}
