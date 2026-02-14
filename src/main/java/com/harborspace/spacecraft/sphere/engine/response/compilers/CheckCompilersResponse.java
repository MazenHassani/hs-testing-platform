package com.harborspace.spacecraft.sphere.engine.response.compilers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.harborspace.spacecraft.sphere.engine.response.CompilerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckCompilersResponse {
    private int id;
    private boolean executing;
    private String date;
    private CompilerInfo compiler;
    private CheckResult result;
    private String uri;
}
