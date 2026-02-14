package com.harborspace.spacecraft.scoreboard.request;

import com.harborspace.spacecraft.scoreboard.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionRequest {
    private long submissionApiId;
    private String userId;
    private String receivedName;
    private String receivedEmail;
    private long testQuestionId;
    private boolean accepted;
    private int score;
    private SubmissionStatus status = SubmissionStatus.WAITING;
    private String submissionJson;
}
