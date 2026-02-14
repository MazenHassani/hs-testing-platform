package com.harborspace.spacecraft.sphere.engine.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Json with verifiable code information")
public class CheckCodeRequest {
    @Schema(description = "Verification code.",
            example = "<int main()\n" +
                    "    {\n" +
                    "        printf(\"Hello\");\n" +
                    "        return 0;\n" +
                    "    }>")
    private String source;

    @Schema(description = "Compiler id from /code/v1/compilers/compilers.",
            example = "1")
    private Integer compilerId = 1;

    @Schema(description = "Input data.", example = " 10 500 ")
    private String input;
}
