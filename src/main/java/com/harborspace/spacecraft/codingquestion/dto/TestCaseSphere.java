package com.harborspace.spacecraft.codingquestion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCaseSphere {
    private Integer id;
    private String input;
    private String output;
    private Integer timeLimit = 1;
    private Integer judgeId;
    private Boolean active = true;

}
