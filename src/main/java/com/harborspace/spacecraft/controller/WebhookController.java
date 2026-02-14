package com.harborspace.spacecraft.controller;

import com.harborspace.spacecraft.ApiHelper;
import com.harborspace.spacecraft.scoreboard.request.SubmissionRequest;
import com.harborspace.spacecraft.scoreboard.service.ScoreboardService;
import com.harborspace.spacecraft.webhook.request.WebhookSubmission;
import com.harborspace.spacecraft.webhook.service.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiHelper.API_WEBHOOK_PREFIX)
@Validated
public class WebhookController {

    @Autowired
    private ScoreboardService scoreboardService;

    @Autowired
    private WebhookService webhookService;

    @PostMapping("/")
    public ResponseEntity putSubmission(@RequestBody WebhookSubmission submission) {
        System.out.println(submission.toString());
        if("sepw".equals(submission.getOrigin())) {
            try {
                SubmissionRequest request = webhookService.convertFormat(submission);
                System.out.println(request.toString());
                scoreboardService.save(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok().build();
    }
}
