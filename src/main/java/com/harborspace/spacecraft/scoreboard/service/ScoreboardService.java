package com.harborspace.spacecraft.scoreboard.service;

import com.harborspace.spacecraft.scoreboard.model.Submission;
import com.harborspace.spacecraft.scoreboard.request.SubmissionRequest;
import com.harborspace.spacecraft.scoreboard.response.ScoreboardResponse;
import com.harborspace.spacecraft.scoreboard.response.UserQuestionScoreResponse;

public interface ScoreboardService {

    Submission save(SubmissionRequest request);

    UserQuestionScoreResponse getQuestionScore(String userId, long testQuestionId);

    ScoreboardResponse getScoreboard(long testId);
}
