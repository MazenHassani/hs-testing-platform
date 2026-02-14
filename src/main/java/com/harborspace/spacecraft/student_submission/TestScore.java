package com.harborspace.spacecraft.student_submission;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TestScore {
    private long testId;
    private float score;
    private List <TaskScore> tasksScore = new ArrayList<>();

}
