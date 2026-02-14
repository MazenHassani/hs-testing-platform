package com.harborspace.spacecraft.codingquestion.service.impl;

import com.harborspace.spacecraft.codingquestion.dto.CodingQuestionSphere;
import com.harborspace.spacecraft.codingquestion.dto.JudgeSphere;
import com.harborspace.spacecraft.codingquestion.dto.request.CodingQuestionRequest;
import com.harborspace.spacecraft.codingquestion.dto.TestCaseSphere;
import com.harborspace.spacecraft.codingquestion.model.CodingQuestion;
import com.harborspace.spacecraft.codingquestion.model.Judge;
import com.harborspace.spacecraft.codingquestion.model.TestCase;
import com.harborspace.spacecraft.codingquestion.repository.CodingQuestionRepository;
import com.harborspace.spacecraft.codingquestion.repository.JudgeRepository;
import com.harborspace.spacecraft.codingquestion.repository.TestCaseRepository;
import com.harborspace.spacecraft.codingquestion.dto.response.CodingQuestionResponse;
import com.harborspace.spacecraft.codingquestion.dto.response.JudgeResponse;
import com.harborspace.spacecraft.codingquestion.dto.response.TestCaseResponse;
import com.harborspace.spacecraft.codingquestion.service.SphereQuestionService;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.sphere.engine.exception.ConvertException;
import com.harborspace.spacecraft.sphere.engine.exception.NotFoundException;
import com.harborspace.spacecraft.sphere.engine.exception.SphereClientException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SphereQuestionServiceImpl implements SphereQuestionService {

    @Autowired
    private CodingQuestionRepository codingQuestionRepository;

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private JudgeRepository judgeRepository;

    private final RestTemplate restTemplate;
    @Value("${sphere.version}")
    private String version;

    @Value("${sphere.endpoint}")
    private String endpoint;

    @Value("${sphere.problems.token}")
    private String problemsToken;

    @Autowired
    private ModelMapper modelMapper;


    public CodingQuestionSphere findOne(Integer id) throws SphereClientException, ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var uri = createURI("problems/" + id);
        var responseEntity = restTemplate.exchange(new RequestEntity<>(HttpMethod.GET, uri), CodingQuestionResponse.class);
        Optional<CodingQuestion> codingQuestion = codingQuestionRepository.findById(id);
        if (codingQuestion.isEmpty()) {
            throw new ResourceNotFoundException("Coding Question");
        }
        CodingQuestionResponse codingQuestionResponse = responseEntity.getBody();
        CodingQuestionSphere codingQuestionSphere = CodingQuestionSphere.of(codingQuestionResponse);
        CodingQuestionSphere codingQuestionSphereSaved = modelMapper.map(codingQuestion.get(), CodingQuestionSphere.class);

        if (!codingQuestionSphereSaved.equals(codingQuestionSphere)) {
            throw new SphereClientException("Coding Question Inconsistent", HttpStatus.CONFLICT.toString());
        }
        return codingQuestionSphere;
    }

    public JudgeResponse findOneJudge(Integer id) throws SphereClientException, ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var uri = createURI("judges/" + id);
        var responseEntity = restTemplate.exchange(new RequestEntity<>(HttpMethod.GET, uri), JudgeResponse.class);
        return responseEntity.getBody();
    }

    public TestCaseSphere findOneTestCase(Integer id, Integer number) throws SphereClientException, ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var uri = createURI("problems/" + id+"/testcases/"+number);
        var responseEntity = restTemplate.exchange(new RequestEntity<>(HttpMethod.GET, uri), TestCaseResponse.class);
        Optional<TestCase> testCase = testCaseRepository.findByIdAndCodingQuestionId(number, id);
        if (testCase.isEmpty()) {
            throw new ResourceNotFoundException("Coding Question Test Case");
        }
        TestCaseResponse testCaseResponse = responseEntity.getBody();
        TestCaseSphere testCaseSphereSaved = modelMapper.map(testCase.get(), TestCaseSphere.class);

        if (!testCaseResponse.getNumber().equals(testCaseSphereSaved.getId())) {
            throw new SphereClientException("Coding Question Test Case Inconsistent", HttpStatus.CONFLICT.toString());
        }
        return testCaseSphereSaved;
    }

    public CodingQuestionSphere create(CodingQuestionRequest codingQuestionRequest) throws SphereClientException {
        CodingQuestionSphere codingQuestionSphere = codingQuestionRequest.getQuestionSphere();
        JudgeSphere judgeSphere = codingQuestionRequest.getJudge();
        codingQuestionSphere.setId(null);
        CodingQuestion codingQuestion = modelMapper.map(codingQuestionSphere, CodingQuestion.class);

        if (judgeSphere != null) {
            Judge createdMasterJudge = createJudge(judgeSphere);
            codingQuestion.setJudge(createdMasterJudge);
            codingQuestion.setMasterjudgeId(createdMasterJudge.getId());
            judgeSphere.setTypeId(0);
            Judge createdJudge = createJudge(judgeSphere);
            codingQuestion.setTestCaseJudge(createdJudge);
            codingQuestion.setTestCaseJudgeId(createdJudge.getId());
        } else codingQuestion.setMasterjudgeId(0);


        CodingQuestionSphere savedCodingQuestionSphere = modelMapper.map(codingQuestion, CodingQuestionSphere.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var uri = createURI("problems");
        var responseEntity = restTemplate.postForEntity(uri, new HttpEntity<>(savedCodingQuestionSphere, headers), Map.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            codingQuestion.setId((Integer) responseEntity.getBody().get("id"));
            return modelMapper.map(codingQuestionRepository.save(codingQuestion), CodingQuestionSphere.class);
        } else throw new NotFoundException("Sphere Question");
    }

    public Judge createJudge(JudgeSphere judgeSphere) throws SphereClientException {
        judgeSphere.setId(null);
        Judge judge = modelMapper.map(judgeSphere, Judge.class);
        JudgeSphere judge1 = modelMapper.map(judge, JudgeSphere.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var uri = createURI("judges");
        var responseEntity = restTemplate.postForEntity(uri, new HttpEntity<>(judge1, headers), Map.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            judge.setId((Integer) responseEntity.getBody().get("id"));
            return judgeRepository.save(judge);
        } else throw new NotFoundException("Sphere Judge");
    }

    public TestCaseSphere addTestCase(Integer id, TestCaseSphere testCaseSphere) throws SphereClientException, ResourceNotFoundException {
        testCaseSphere.setId(null);
        testCaseSphere.setJudgeId(null);
        TestCase testCase = modelMapper.map(testCaseSphere, TestCase.class);
        Optional<CodingQuestion> codingQuestionOpt = codingQuestionRepository.findById(id);
        if (codingQuestionOpt.isEmpty()) {
            throw new ResourceNotFoundException("Coding Question");
        }
        testCase.setJudgeId(codingQuestionOpt.get().getTestCaseJudgeId());
        TestCaseSphere savedTestCaseSphere = modelMapper.map(testCase, TestCaseSphere.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var uri = createURI("problems/" + id + "/testcases");
        var responseEntity = restTemplate.postForEntity(uri, new HttpEntity<>(savedTestCaseSphere, headers), Map.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            testCase.setId((Integer) responseEntity.getBody().get("number"));
            testCase.setCodingQuestion(codingQuestionOpt.get());
            return modelMapper.map(testCaseRepository.save(testCase), TestCaseSphere.class);
        } else throw new NotFoundException("Sphere Test Case");

    }

    public CodingQuestionSphere update(Integer id, CodingQuestionRequest codingQuestionRequest) throws ResourceNotFoundException {
        CodingQuestionSphere codingQuestionSphere = codingQuestionRequest.getQuestionSphere();
        JudgeSphere judgeSphere = codingQuestionRequest.getJudge();
        Optional<CodingQuestion> codingQuestionOpt = codingQuestionRepository.findById(id);
        if (codingQuestionOpt.isEmpty()) {
            throw new ResourceNotFoundException("Coding Question");
        }
        codingQuestionSphere.setId(id);
        CodingQuestion codingQuestion = codingQuestionOpt.get();
        BeanUtils.copyProperties(codingQuestionSphere, codingQuestion);
        if (judgeSphere != null) {
            Judge updatedMasterJudge = updateJudge(codingQuestion.getMasterjudgeId(), judgeSphere);
            codingQuestion.setJudge(updatedMasterJudge);
            codingQuestion.setMasterjudgeId(updatedMasterJudge.getId());
            judgeSphere.setTypeId(0);
            Judge updatedTestCaseJudge = updateJudge(codingQuestion.getTestCaseJudgeId(), judgeSphere);
            codingQuestion.setTestCaseJudge(updatedTestCaseJudge);
            codingQuestion.setTestCaseJudgeId(updatedTestCaseJudge.getId());
        }
        CodingQuestionSphere updatedCodingQuestionSphere = modelMapper.map(codingQuestion, CodingQuestionSphere.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uri = createURI("problems/" + id);
        var responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(updatedCodingQuestionSphere, headers), String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return updatedCodingQuestionSphere;
        } else throw new NotFoundException("Sphere Question");
    }

    public Judge updateJudge(Integer id, JudgeSphere judgeSphere) throws SphereClientException, ResourceNotFoundException {
        Optional<Judge> judgeOpt = judgeRepository.findById(id);
        if (judgeOpt.isEmpty()) {
            throw new ResourceNotFoundException("Coding Judge");
        }
        judgeSphere.setId(id);
        Judge updatedJudge = judgeOpt.get();
        BeanUtils.copyProperties(judgeSphere, updatedJudge);

        JudgeSphere judge1 = modelMapper.map(updatedJudge, JudgeSphere.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var uri = createURI("judges/" + id);
        var responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(judge1, headers), String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return updatedJudge;
        } else throw new NotFoundException("Sphere Judge");
    }

    public TestCaseSphere updateTestCase(Integer codingQuestionId, Integer testCaseId, TestCaseSphere testCaseSphere) throws ResourceNotFoundException {
        Optional<TestCase> testCaseOpt = testCaseRepository.findByIdAndCodingQuestionId(testCaseId, codingQuestionId);
        if (testCaseOpt.isEmpty()) {
            throw new ResourceNotFoundException("Coding Test Case");
        }
        testCaseSphere.setId(testCaseId);
        TestCase updatedJudge = testCaseOpt.get();
        BeanUtils.copyProperties(testCaseSphere, updatedJudge);

        TestCaseSphere updatedTestCaseSphere = modelMapper.map(testCaseSphere, TestCaseSphere.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uri = createURI("problems/" + codingQuestionId + "/testcases/" + testCaseId);
        var responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(updatedTestCaseSphere, headers), String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return updatedTestCaseSphere;
        } else throw new NotFoundException("Sphere Question's Test Case");
    }

    public void delete(Integer id) {
        if (codingQuestionRepository.existsById(id)) {
            codingQuestionRepository.deleteById(id);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uri = createURI("problems/" + id);
        restTemplate.delete(uri);
    }


    public void deleteTestCase(Integer codingQuestionId, Integer testCaseId) {
        if (testCaseRepository.existsByIdAndCodingQuestionId(testCaseId, codingQuestionId)) {
            testCaseRepository.deleteByIdAndCodingQuestionId(testCaseId, codingQuestionId);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uri = createURI("problems/" + codingQuestionId + "/testcases/" + testCaseId);
        restTemplate.delete(uri);
    }

    private URI createURI(String dest) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("access_token", problemsToken);
        var urlS = "https://" + endpoint + ".problems.sphere-engine.com/api/" + version + "/" + dest;
        return URI.create(UriComponentsBuilder.fromHttpUrl(urlS).queryParams(params).toUriString());
    }

    private <T> T convert(ResponseEntity<String> responseEntity, Class<T> valueType) throws SphereClientException {
        if (responseEntity == null || responseEntity.getBody() == null) {
            throw new ConvertException("Sphere Resource");
        }
        return modelMapper.map(responseEntity.getBody(), valueType);
    }

}
