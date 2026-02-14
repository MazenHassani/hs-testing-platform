package com.harborspace.spacecraft.controller;

import com.harborspace.spacecraft.sphere.engine.ServiceType;
import com.harborspace.spacecraft.sphere.engine.request.CheckCodeProblemsRequest;
import com.harborspace.spacecraft.sphere.engine.request.CheckCodeRequest;
import com.harborspace.spacecraft.sphere.engine.response.*;
import com.harborspace.spacecraft.sphere.engine.response.compilers.CheckCompilersResponse;
import com.harborspace.spacecraft.sphere.engine.response.problems.CheckProblemsResponse;
import com.harborspace.spacecraft.sphere.engine.sevice.SphereEngineClient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import static com.harborspace.spacecraft.ApiHelper.API_SPHERE_ENGINE_PREFIX;

@RestController
@RequestMapping(API_SPHERE_ENGINE_PREFIX)
@RequiredArgsConstructor
public class SphereEngineController {
    public static final int MAX_LIMIT = 200;
    private final SphereEngineClient sphereEngineClient;

    @Operation(
            summary = "Send verification code in the context of \"Compilers\".",
            description = "Send verification code and create submission.")
    @PostMapping("/compilers/submissions")
    public SubmissionsResponse sendSubmissionsCompilers(@RequestBody CheckCodeRequest request) {
        return sphereEngineClient.submissionCreate(request);
    }

    @Operation(
            summary = "Send verification codes in the context of \"Problems\".",
            description = "Send verification code and create submission.")
    @PostMapping("/problems/submissions")
    public SubmissionsResponse sendSubmissionsProblems(@RequestBody CheckCodeProblemsRequest request) {
        return sphereEngineClient.submissionCreate(request);
    }

    @Operation(
            summary = "The list of available compilers in the context of \"Compilers\".",
            description = "Details of available compilers.")
    @GetMapping("/compilers/compilers")
    public ListResponse<CompilerResponse> getCompilersCompilers(@RequestParam(name = "offset", defaultValue = "0") int offset,
                                                                @RequestParam(name = "limit", defaultValue = "50") int limit) {
        if (limit > MAX_LIMIT) {
            limit = MAX_LIMIT;
        }
        return ListResponse.fromListUsingOffsetLimit(sphereEngineClient.compilers(ServiceType.COMPILERS), offset, limit);
    }

    @Operation(
            summary = "The list of available compilers in the context of \"Problems\".",
            description = "Details of available compilers.")
    @GetMapping("/problems/compilers")
    public ListResponse<CompilerResponse> getCompilersProblems(@RequestParam(name = "offset", defaultValue = "0") int offset,
                                                               @RequestParam(name = "limit", defaultValue = "50") int limit) {
        if (limit > MAX_LIMIT) {
            limit = MAX_LIMIT;
        }
        return ListResponse.fromListUsingOffsetLimit(sphereEngineClient.compilers(ServiceType.PROBLEMS), offset, limit);
    }

    @Operation(
            summary = "Submission status in the context of \"Compilers\".",
            description = "Retrieving the data of submission  defining the current status of the submission.")
    @GetMapping("/compilers/{submissionId}/check")
    public CheckCompilersResponse submissionStatusCompilers(
            @Parameter(
                    description = "Submission ID.",
                    required = true,
                    example = "108529817")
            @PathVariable Long submissionId) {
        return sphereEngineClient.submissionStatusCompilers(submissionId);
    }

    @Operation(
            summary = "Submission result in the context of \"Problems\".",
            description = "Retrieving the data of submission  defining the result of the submission.")
    @GetMapping("/problems/{submissionId}/result")
    public String submissionResult(
            @Parameter(
                    description = "Submission ID.",
                    required = true,
                    example = "7661156")
            @PathVariable Long submissionId) {
        return sphereEngineClient.getSubmissionCode(submissionId);
    }

    @Operation(
            summary = "Submission status in the context of \"Problems\".",
            description = "Retrieving the data of submission  defining the current status of the submission.")
    @GetMapping("/problems/{submissionId}/check")
    public CheckProblemsResponse submissionStatusProblems(
            @Parameter(
                    description = "Submission ID.",
                    required = true,
                    example = "7661156")
            @PathVariable Long submissionId) {
        return sphereEngineClient.submissionStatusProblems(submissionId);
    }

    @Operation(summary = "Availability сheck Compilers API.", description = "Every successful usage returns identical response.")
    @GetMapping("/compilers/test")
    public TestResponse getTestCompilers() {
        return sphereEngineClient.test(ServiceType.COMPILERS);
    }

    @Operation(summary = "Availability сheck Problems API.", description = "Every successful usage returns identical response.")
    @GetMapping("/problems/test")
    public TestResponse getTestProblems() {
        return sphereEngineClient.test(ServiceType.PROBLEMS);
    }

}
