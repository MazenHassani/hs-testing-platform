package com.harborspace.spacecraft.scoreboard.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.harborspace.spacecraft.scoreboard.SubmissionStatus;
import com.harborspace.spacecraft.scoreboard.model.UserQuestionScore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserQuestionScoreResponse {
    private long testQuestionId;
    private long submissionId;
    private long submissionApiId;
    private long questionId;
    private SubmissionStatus status;
    private int score;
    private boolean isAccepted;

    public void of(UserQuestionScore questionScore) {
        if (questionScore != null) {
            var bestSubmission = questionScore.getBestSubmission();
            if (bestSubmission != null) {
                this.testQuestionId = bestSubmission.getTestQuestion().getId();
                this.submissionId = bestSubmission.getId();
                this.submissionApiId = bestSubmission.getSubmissionApiId();
                this.status = bestSubmission.getStatus();
                this.score = bestSubmission.getScore();
                this.isAccepted = bestSubmission.getIsAccepted();
                this.questionId = bestSubmission.getTestQuestion().getQuestionId();
            }
        }
    }
}
