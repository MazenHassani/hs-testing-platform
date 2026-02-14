package com.harborspace.spacecraft.codingquestion.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.harborspace.spacecraft.codingquestion.dto.CodingQuestionSphere;
import com.harborspace.spacecraft.codingquestion.dto.JudgeSphere;
import com.harborspace.spacecraft.codingquestion.model.Judge;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
