package com.harborspace.spacecraft.sphere.engine.response.compilers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.harborspace.spacecraft.sphere.engine.response.CheckStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CheckResult {
    private CheckStatus status;
    private long time;
    private long memory;
    private int signal;
    private String signalDesc;
    private CheckStreams streams;
}
