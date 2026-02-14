package com.harborspace.spacecraft.sphere.engine.response.problems;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.harborspace.spacecraft.sphere.engine.response.CompilerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckProblemsResponse {
    private int id;
    private boolean executing;
    private String date;
    private CompilerInfo compiler;
    private ProblemInfo problem;
    private CheckResult result;
}
