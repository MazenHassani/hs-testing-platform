package com.harborspace.spacecraft.controller;

import com.harborspace.spacecraft.ApiHelper;
import com.harborspace.spacecraft.core.UserFilter;
import com.harborspace.spacecraft.course.dto.CourseDto;
import com.harborspace.spacecraft.course.dto.TestDto;
import com.harborspace.spacecraft.course.service.CourseService;
import com.harborspace.spacecraft.course.service.TestService;
import com.harborspace.spacecraft.exception.AlreadyStartedException;
import com.harborspace.spacecraft.exception.InvalidRequestException;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.student_submission.StudentScore;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiHelper.API_COURSE_PREFIX)
@Validated
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private TestService testService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('course_manager') or @securityService.isParticipant(#id, authentication)")
    public ResponseEntity<CourseDto> findOne(@PathVariable("id") Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(courseService.findOne(id), HttpStatus.OK);
    }

    @GetMapping("/")
    @PostFilter("hasRole('course_manager') or @securityService.isParticipant(filterObject.id, authentication)")
    public List<CourseDto> findAll() {
        return courseService.findAll();
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('course_manager')")
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseDto newCourse) throws InvalidRequestException {
        if (newCourse.getEndDate().before(newCourse.getStartDate())) {
            throw new InvalidRequestException("EndDate");
        }
        return new ResponseEntity<>(courseService.create(newCourse), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('course_manager') or @securityService.isTeacher(#id, authentication)")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable("id") Long id, @Valid @RequestBody CourseDto newCourse,
                                                  @RequestAttribute(UserFilter.USER_ID) String userId) throws ResourceNotFoundException, InvalidRequestException {
        if (newCourse.getEndDate().before(newCourse.getStartDate())) {
            throw new InvalidRequestException("EndDate");
        }
        newCourse.setId(id);
        CourseDto courseDto = courseService.update(newCourse);
        return new ResponseEntity<>(courseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('course_manager')")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) throws AlreadyStartedException {
        courseService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // TEST

    @PostMapping("/{id}/test")
    @PreAuthorize("hasRole('course_manager') or @securityService.isTeacher(#id, authentication)")
    public ResponseEntity<TestDto> createTest(@PathVariable("id") Long id, @Valid @RequestBody TestDto newTest) throws ResourceNotFoundException, InvalidRequestException {
        if (newTest.getEndDate().before(newTest.getStartDate())) {
            throw new InvalidRequestException("EndDate");
        }
        return new ResponseEntity<>(testService.create(id, newTest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/test")
    @PreAuthorize("hasRole('course_manager') or @securityService.isParticipant(#id, authentication)")
    public ResponseEntity<Page<TestDto>> findAllByCourse(
            @PathVariable("id") Long id,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return new ResponseEntity<>(testService.findAllByCourse(id, page, size), HttpStatus.OK);
    }

    @GetMapping("/{course_id}/test/{id}")
    @PreAuthorize("hasRole('course_manager') or @securityService.isParticipant(#course_id, authentication)")
    public ResponseEntity<TestDto> findOneTest(@PathVariable("id") Long id,
                                               @PathVariable("course_id") Long course_id) throws ResourceNotFoundException {
        return new ResponseEntity<>(testService.findOne(course_id, id), HttpStatus.OK);
    }

    @PutMapping("/{course_id}/test/{id}")
    @PreAuthorize("hasRole('course_manager') or @securityService.isTeacher(#course_id, authentication)")
    public ResponseEntity<TestDto> updateTest(@PathVariable Long id,
                                              @PathVariable("course_id") Long course_id,
                                              @Valid @RequestBody TestDto newTest) throws ResourceNotFoundException, InvalidRequestException {
        if (newTest.getEndDate().before(newTest.getStartDate())) {
            throw new InvalidRequestException("EndDate");
        }
        newTest.setId(id);
        TestDto testDto = testService.update(newTest);
        return new ResponseEntity<>(testDto, HttpStatus.OK);
    }

    @DeleteMapping("/{course_id}/test/{id}")
    @PreAuthorize("hasRole('course_manager') or @securityService.isTeacher(#course_id, authentication)")
    public ResponseEntity<String> deleteTest(@PathVariable Long id,
                                             @PathVariable("course_id") Long course_id) throws AlreadyStartedException {
        testService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{course_id}/test/{id}/scoreboard")
    @PreAuthorize("hasRole('course_manager') or @securityService.isParticipant(#course_id, authentication)")
    public ResponseEntity<List<StudentScore>> findOneTestScoreboard(@PathVariable("id") Long id,
                                               @PathVariable("course_id") Long course_id) throws ResourceNotFoundException {
        return new ResponseEntity<>(testService.getScores(course_id, id), HttpStatus.OK);
    }

}
