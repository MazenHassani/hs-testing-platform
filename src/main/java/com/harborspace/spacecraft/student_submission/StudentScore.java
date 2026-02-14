package com.harborspace.spacecraft.student_submission;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentScore {
    private String studentId;
    private List<TestScore> testsScore = new ArrayList<>();
}
