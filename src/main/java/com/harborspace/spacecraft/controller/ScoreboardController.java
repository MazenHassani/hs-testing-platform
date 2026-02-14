package com.harborspace.spacecraft.controller;

import com.harborspace.spacecraft.scoreboard.request.SubmissionRequest;
import com.harborspace.spacecraft.scoreboard.response.ScoreboardResponse;
import com.harborspace.spacecraft.scoreboard.response.UserQuestionScoreResponse;
import com.harborspace.spacecraft.scoreboard.service.ScoreboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.harborspace.spacecraft.ApiHelper.API_SCOREBOARD_PREFIX;

@RestController
@RequestMapping(API_SCOREBOARD_PREFIX)
@RequiredArgsConstructor
public class ScoreboardController {
    private final ScoreboardService scoreboardService;

    @PostMapping("/add")
    public void addRawSubmission(@RequestBody SubmissionRequest request) {
        scoreboardService.save(request);
    }

    @GetMapping("/{userId}/user_question_score/{testQuestionId}")
    public UserQuestionScoreResponse getUserQuestionScore(@PathVariable String userId, @PathVariable long testQuestionId) {
        return scoreboardService.getQuestionScore(userId, testQuestionId);
    }

    @GetMapping("/test/{testId}")
    public ScoreboardResponse getScoreboard(@PathVariable long testId) {
        return scoreboardService.getScoreboard(testId);
    }
}
