package com.harborspace.spacecraft.sphere.engine.sevice;

import com.harborspace.spacecraft.sphere.engine.ServiceType;
import com.harborspace.spacecraft.sphere.engine.request.CheckCodeProblemsRequest;
import com.harborspace.spacecraft.sphere.engine.request.CheckCodeRequest;
import com.harborspace.spacecraft.sphere.engine.response.CompilerResponse;
import com.harborspace.spacecraft.sphere.engine.response.SubmissionsResponse;
import com.harborspace.spacecraft.sphere.engine.response.TestResponse;
import com.harborspace.spacecraft.sphere.engine.response.compilers.CheckCompilersResponse;
import com.harborspace.spacecraft.sphere.engine.response.problems.CheckProblemsResponse;

import java.util.List;

public interface SphereEngineClient {
    SubmissionsResponse submissionCreate(CheckCodeRequest request);

    SubmissionsResponse submissionCreate(CheckCodeProblemsRequest request);

    CheckCompilersResponse submissionStatusCompilers(long submissionId);

    CheckProblemsResponse submissionStatusProblems(long submissionId);

    TestResponse test(ServiceType type);

    String getSubmissionCode(Long submissionId);

    List<CompilerResponse> compilers(ServiceType type);

}
