package com.harborspace.spacecraft.sphere.engine.response.problems;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckStreams {
    private Object source;
    private Object output;
    private Object error;
    private Object cmpinfo;
    private Object debug;
    private Object input;
}
