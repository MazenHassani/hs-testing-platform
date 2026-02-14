package com.harborspace.spacecraft.sphere.engine.response.problems;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemInfo {
    private int id;
    private String code;
    private String name;
    private String uri;
}
