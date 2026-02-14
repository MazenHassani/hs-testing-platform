package com.harborspace.spacecraft.codingquestion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.harborspace.spacecraft.codingquestion.dto.response.CodingQuestionResponse;
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
public class CodingQuestionSphere {
    private Integer id;

    private String name;
    private String body; // input + output + legend + notes + scoring
    private Float timeLimit;
    private Integer typeId = 0;
    private Integer masterjudgeId;

    private Boolean interactive = false;

    public static CodingQuestionSphere of(CodingQuestionResponse response) {
        return new CodingQuestionSphere().setBody(response.getBody())
                .setId(response.getId()).setInteractive(response.getInteractive())
                .setMasterjudgeId(response.getMasterjudge().getId())
                .setName(response.getName()).setTypeId(response.getType().getId());
    }

}
