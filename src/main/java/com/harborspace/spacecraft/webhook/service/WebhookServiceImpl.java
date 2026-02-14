package com.harborspace.spacecraft.webhook.service;

import com.harborspace.spacecraft.scoreboard.SubmissionStatus;
import com.harborspace.spacecraft.scoreboard.request.SubmissionRequest;
import com.harborspace.spacecraft.webhook.request.WebhookSubmission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebhookServiceImpl implements WebhookService {

    public SubmissionRequest convertFormat(WebhookSubmission webhookSubmission) {
        SubmissionRequest request = new SubmissionRequest();
        SubmissionStatus status = SubmissionStatus.fromCode(webhookSubmission.getSubmission().getResult().getStatus().getCode());
        long testQuestionId = Long.parseLong(webhookSubmission.getWidget().getCustom_data());
        request.setTestQuestionId(testQuestionId);
        request.setAccepted(SubmissionStatus.SUCCESS.equals(status));
        request.setReceivedName(webhookSubmission.getWidget().getUser().getName());
        request.setReceivedEmail(webhookSubmission.getWidget().getUser().getEmail());
        request.setUserId(webhookSubmission.getWidget().getUser().getId());
        request.setScore(webhookSubmission.getSubmission().getResult().getScore());
        request.setStatus(status);
        request.setSubmissionApiId(webhookSubmission.getSubmission().getId());
        return request;
    }

}
