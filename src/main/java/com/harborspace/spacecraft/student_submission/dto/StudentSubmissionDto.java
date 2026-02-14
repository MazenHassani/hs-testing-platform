package com.harborspace.spacecraft.student_submission.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.harborspace.spacecraft.student_submission.model.StudentSubmission;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentSubmissionDto {
    private Long taskId;
    private String value;
    private boolean correct;
    public static StudentSubmissionDto of(StudentSubmission tq) {
        return new StudentSubmissionDto().setTaskId(tq.getTask().getId())
                .setValue(tq.getVal()).setCorrect(tq.getCorrectVal());
    }
}
