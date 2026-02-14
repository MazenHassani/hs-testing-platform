package com.harborspace.spacecraft.course.service.impl;

import com.harborspace.spacecraft.course.dto.TestDto;
import com.harborspace.spacecraft.course.service.TestService;
import com.harborspace.spacecraft.exception.AlreadyStartedException;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.student_submission.StudentScore;
import com.harborspace.spacecraft.course.model.Course;
import com.harborspace.spacecraft.course.model.Test;
import com.harborspace.spacecraft.course.repository.CourseRepository;
import com.harborspace.spacecraft.course.repository.TestRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public TestDto findOne(Long courseId, Long id) throws ResourceNotFoundException {
        return testRepository.findById(id)
                .filter(test -> test.getCourse() != null && courseId.equals(test.getCourse().getId()))
                .map(test -> modelMapper.map(test,
                        TestDto.class))
                .orElseThrow(() -> new ResourceNotFoundException("Test"));
    }

    @Override
    public Page<TestDto> findAllByCourse(Long courseId, int page, int pageSize) {
        if (page < 0)
            page = 0;
        if (pageSize < 5)
            pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Test> tests = testRepository.findAllByCourseId(courseId, pageable);
        return tests.map(test -> modelMapper.map(test, TestDto.class));
    }

    @Override
    public Page<TestDto> findAll(int page, int pageSize) {
        if (page < 0)
            page = 0;
        if (pageSize < 5)
            pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Test> tests = testRepository.findAll(pageable);
        return tests.map(test -> modelMapper.map(test, TestDto.class));
    }

    @Override
    public TestDto create(Long courseId, TestDto testDto) throws ResourceNotFoundException {
        Test test = modelMapper.map(testDto, Test.class);
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isEmpty()) {
            throw new ResourceNotFoundException("Course");
        }
        Course course = courseOpt.get();
        test.setCourse(course);
        test.setId(null);
        Test savedTest = testRepository.save(test);
        return modelMapper.map(savedTest, TestDto.class);
    }

    @Override
    public TestDto update(TestDto testDto) throws ResourceNotFoundException {
        Optional<Test> testOpt = testRepository.findById(testDto.getId());
        if (testOpt.isEmpty())
            throw new ResourceNotFoundException("Test");
        Test updatedTest = testOpt.get();
        BeanUtils.copyProperties(testDto, updatedTest);
        return modelMapper.map(testRepository.save(updatedTest), TestDto.class);
    }

    @Override
    public void deleteById(Long id) throws AlreadyStartedException {
        Optional<Test> testOpt = testRepository.findById(id);
        if (testOpt.isPresent()) {
            Test test = testOpt.get();
            if (test.getStartDate() == null || new Date().after(test.getStartDate())) {
                throw new AlreadyStartedException("Test");
            }
            testRepository.deleteById(id);
        }
    }

    @Override
    public List <StudentScore> getScores(Long courseId, Long id) throws ResourceNotFoundException {
        Test theTest = testRepository.findById(id)
                .filter(test -> test.getCourse() != null && courseId.equals(test.getCourse().getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Test"));
        return theTest.getTestScores();
    }
    @Override
    public StudentScore getScoresByStudentId(Long courseId, Long testId, String studentId) throws ResourceNotFoundException {
        Test theTest = testRepository.findById(testId)
                .filter(test -> test.getCourse() != null && courseId.equals(test.getCourse().getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Test"));
        return theTest.getTestScores().stream().filter(s -> s.getStudentId().equals(studentId)).findFirst().orElseThrow(() -> new ResourceNotFoundException("No Score for this Student"));
    }
}
