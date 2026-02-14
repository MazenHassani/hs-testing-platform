package com.harborspace.spacecraft.sphere.engine.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompilerResponse {
    private int id;

    private String name;

    @JsonProperty("short")
    private String shortName;

    private List<CompilerVersion> versions;

    private Object highlighters;


}
