package com.harborspace.spacecraft.student_submission;

import com.harborspace.spacecraft.testquestion.model.TestQuestion;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TaskScore{
    private long taskId;
    private int score;
}