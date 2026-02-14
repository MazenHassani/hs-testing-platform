package com.harborspace.spacecraft.webhook.service;

import com.harborspace.spacecraft.scoreboard.request.SubmissionRequest;
import com.harborspace.spacecraft.webhook.request.WebhookSubmission;

public interface WebhookService {

    SubmissionRequest convertFormat(WebhookSubmission webhookSubmission);
}
