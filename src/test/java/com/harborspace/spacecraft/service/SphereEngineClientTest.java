package com.harborspace.spacecraft.service;

import com.harborspace.spacecraft.sphere.engine.ServiceType;
import com.harborspace.spacecraft.sphere.engine.exception.NotFoundException;
import com.harborspace.spacecraft.sphere.engine.request.CheckCodeProblemsRequest;
import com.harborspace.spacecraft.sphere.engine.request.CheckCodeRequest;
import com.harborspace.spacecraft.sphere.engine.response.SubmissionsResponse;
import com.harborspace.spacecraft.sphere.engine.response.compilers.CheckCompilersResponse;
import com.harborspace.spacecraft.sphere.engine.sevice.SphereEngineClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class SphereEngineClientTest {
    private static final String CODE = "<int main()\n" +
            "    {\n" +
            "        printf(\"Hello\");\n" +
            "        return 0;\n" +
            "    }>";
    @Autowired
    private SphereEngineClient sphereEngineClient;
    private SubmissionsResponse submissionInCompilers;
    private SubmissionsResponse submissionInProblems;

//    @Test
    public void createSubmissionInCompilers() {
        initSubmissionInCompilers();
        assertNotNull(submissionInCompilers);
        assertTrue(submissionInCompilers.getId() > 0);
        log.info(submissionInCompilers.toString());
    }

//    @Test
    public void createSubmissionInProblems() {
        initSubmissionInProblems();
        assertNotNull(submissionInProblems);
        assertTrue(submissionInProblems.getId() > 0);
        log.info(submissionInProblems.toString());
    }

//    @Test
    public void statusInCompilersById() {
        initSubmissionInCompilers();
        awaitAsyncProcessing();
        assertNotNull(submissionInCompilers);
        CheckCompilersResponse response = sphereEngineClient.submissionStatusCompilers(submissionInCompilers.getId());
        assertNotNull(response);
        log.info(response.toString());
    }

//    @Test
    public void statusInProblemsById() {
        initSubmissionInProblems();
        awaitAsyncProcessing();
        assertNotNull(submissionInProblems);
        var response = sphereEngineClient.submissionStatusProblems(submissionInProblems.getId());
        assertNotNull(response);
        log.info(response.toString());
    }

//    @Test
    public void checkNotFoundExceptionById() {
        initSubmissionInCompilers();
        assertNotNull(submissionInCompilers);
        assertThrows(NotFoundException.class, () -> sphereEngineClient.submissionStatusCompilers(-100500));
    }

//    @Test
    public void availabilityCheckInProblems() {
        var response = sphereEngineClient.test(ServiceType.PROBLEMS);
        assertNotNull(response);
        log.info(response.toString());
    }

//    @Test
    public void availabilityCheckInCompilers() {
        var response = sphereEngineClient.test(ServiceType.COMPILERS);
        assertNotNull(response);
        log.info(response.toString());
    }

    @Test
    public void availableCompilersInCompilers() {
        var response = sphereEngineClient.compilers(ServiceType.COMPILERS);
        assertNotNull(response);
        assertFalse(response.isEmpty());
        log.info(response.toString());
    }

//    @Test
    public void availableCompilersInProblems() {
        var response = sphereEngineClient.compilers(ServiceType.PROBLEMS);
        assertNotNull(response);
        assertFalse(response.isEmpty());
        log.info(response.toString());
    }

    private void initSubmissionInCompilers() {
        if (submissionInCompilers == null) {
            submissionInCompilers = sphereEngineClient.submissionCreate(CheckCodeRequest.builder()
                    .compilerId(1)
                    .source(CODE)
                    .build());
        }
    }

    private void initSubmissionInProblems() {
        if (submissionInProblems == null) {
            submissionInProblems = sphereEngineClient.submissionCreate(CheckCodeProblemsRequest.builder()
                    .problemId(34932)
                    .compilerId(1)
                    .source(CODE)
                    .build());
        }
    }

    private void awaitAsyncProcessing() {
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}
