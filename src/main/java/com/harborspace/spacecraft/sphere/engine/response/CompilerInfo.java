package com.harborspace.spacecraft.sphere.engine.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompilerInfo {
    private Integer id;
    private String name;
    private CompilerVersion version;
}
