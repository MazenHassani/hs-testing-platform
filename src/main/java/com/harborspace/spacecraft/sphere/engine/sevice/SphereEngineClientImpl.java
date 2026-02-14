package com.harborspace.spacecraft.sphere.engine.sevice;//import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harborspace.spacecraft.sphere.engine.ServiceType;
import com.harborspace.spacecraft.sphere.engine.exception.*;
import com.harborspace.spacecraft.sphere.engine.request.CheckCodeProblemsRequest;
import com.harborspace.spacecraft.sphere.engine.request.CheckCodeRequest;
import com.harborspace.spacecraft.sphere.engine.response.*;
import com.harborspace.spacecraft.sphere.engine.response.compilers.CheckCompilersResponse;
import com.harborspace.spacecraft.sphere.engine.response.problems.CheckProblemsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SphereEngineClientImpl implements SphereEngineClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${sphere.version}")
    private String version;

    @Value("${sphere.endpoint}")
    private String endpoint;

    @Value("${sphere.problems.token}")
    private String problemsToken;

    @Value("${sphere.compilers.token}")
    private String compilersToken;

    @Override
    public SubmissionsResponse submissionCreate(CheckCodeRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var uri = createURI(ServiceType.COMPILERS, "submissions", initParams(ServiceType.COMPILERS));
        var responseEntity = restTemplate.postForEntity(uri, new HttpEntity<>(request, headers), String.class);
        return convert(responseEntity, SubmissionsResponse.class);
    }

    @Override
    public SubmissionsResponse submissionCreate(CheckCodeProblemsRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var uri = createURI(ServiceType.PROBLEMS, "submissions", initParams(ServiceType.PROBLEMS));
        var responseEntity = restTemplate.postForEntity(uri, new HttpEntity<>(request, headers), String.class);
        return convert(responseEntity, SubmissionsResponse.class);
    }

    @Override
    public CheckCompilersResponse submissionStatusCompilers(long submissionId) {
        var responseEntity = get(ServiceType.COMPILERS, "submissions/" + submissionId, initParams(ServiceType.COMPILERS));
        return convert(responseEntity, CheckCompilersResponse.class);
    }

    @Override
    public CheckProblemsResponse submissionStatusProblems(long submissionId) {
        var responseEntity = get(ServiceType.PROBLEMS, "submissions/" + submissionId, initParams(ServiceType.PROBLEMS));
        return convert(responseEntity, CheckProblemsResponse.class);
    }

    @Override
    public TestResponse test(ServiceType type) {
        var responseEntity = get(type, "test", initParams(type));
        return convert(responseEntity, TestResponse.class);
    }


    @Override
    public List<CompilerResponse> compilers(ServiceType type) {
        var responseEntity = get(type, "compilers", initParams(type));
        return convert(responseEntity, ListCompilerResponse.class).getItems();
    }

    private ResponseEntity<String> get(ServiceType type, String dest, MultiValueMap<String, String> params) {
        try {
            return restTemplate.exchange(new RequestEntity<>(HttpMethod.GET, createURI(type, dest, params)), String.class);
        } catch (HttpClientErrorException e) {
            checkException(e);
        }
        return null;
    }

    @Override
    public String getSubmissionCode(Long submissionId) {
        var responseEntity = get(ServiceType.PROBLEMS, "submissions/" + submissionId + "/source", initParams(ServiceType.PROBLEMS));
        return responseEntity.getBody();
    }

    private void checkException(HttpClientErrorException e) throws SphereClientException {
        if (HttpStatus.NOT_FOUND.equals(e.getStatusCode()))
            throw new NotFoundException(e.getMessage());
        if (HttpStatus.UNAUTHORIZED.equals(e.getStatusCode()))
            throw new NotAuthorizedException(e.getMessage());
        if (HttpStatus.NO_CONTENT.equals(e.getStatusCode()))
            throw new ConnectionException(e.getMessage());
        if (HttpStatus.FORBIDDEN.equals(e.getStatusCode()))
            throw new ForbiddenException(e.getMessage());
        else
            throw e;
    }

    private MultiValueMap<String, String> initParams(ServiceType type) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (ServiceType.COMPILERS.equals(type)) {
            params.add("access_token", compilersToken);
        } else {
            params.add("access_token", problemsToken);
        }

        return params;
    }

    private URI createURI(ServiceType type, String dest, MultiValueMap<String, String> params) {
        var typeStr = ServiceType.COMPILERS.equals(type) ? "compilers" : "problems";
        var urlS = "https://" + endpoint + "." + typeStr + ".sphere-engine.com/api/" + version + "/" + dest;
        return URI.create(UriComponentsBuilder.fromHttpUrl(urlS).queryParams(params).toUriString());
    }

    private <T> T convert(ResponseEntity<String> responseEntity, Class<T> valueType) {
        try {
            if (responseEntity == null || responseEntity.getBody() == null) {
                throw new ConvertException("Empty response entity or body");
            }
            return objectMapper.readValue(responseEntity.getBody(), valueType);
        } catch (IOException e) {
            log.error("Error parsing response.");
            throw new ConvertException(e.getMessage());
        }
    }


}
