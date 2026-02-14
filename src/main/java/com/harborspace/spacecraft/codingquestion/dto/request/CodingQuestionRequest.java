package com.harborspace.spacecraft.codingquestion.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.harborspace.spacecraft.codingquestion.dto.CodingQuestionSphere;
import com.harborspace.spacecraft.codingquestion.dto.JudgeSphere;
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
public class CodingQuestionRequest {
    private CodingQuestionSphere questionSphere;
    private JudgeSphere judge;

}