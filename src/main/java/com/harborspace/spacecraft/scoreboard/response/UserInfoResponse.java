package com.harborspace.spacecraft.scoreboard.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResponse {
    private String userId;

    private List<UserQuestionScoreResponse> scoreResponses = new ArrayList<>();
}
