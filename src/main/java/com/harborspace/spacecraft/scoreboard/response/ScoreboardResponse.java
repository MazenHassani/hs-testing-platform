package com.harborspace.spacecraft.scoreboard.response;

import com.harborspace.spacecraft.testquestion.dto.TestQuestionDto;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ScoreboardResponse {
    private List<UserInfoResponse> userInformation = new ArrayList<>();
    private List<TestQuestionDto> testQuestions = new ArrayList<>();
}
