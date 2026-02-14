package com.harborspace.spacecraft.controller;

import com.harborspace.spacecraft.course.dto.CourseDto;
import com.harborspace.spacecraft.course.dto.TestDto;
import com.harborspace.spacecraft.exception.AlreadyStartedException;
import com.harborspace.spacecraft.exception.InvalidRequestException;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.course.service.CourseService;
import com.harborspace.spacecraft.course.service.TestService;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {

    @InjectMocks
    CourseController courseController;

    @Mock
    CourseService courseService;

    @Mock
    TestService testService;

    @Test
    public void testFindOne() throws ResourceNotFoundException {
        // given
        CourseDto course = new CourseDto();
        course.setId(1L);
        course.setName("Course 1");
        Mockito.when(courseService.findOne(1L)).thenReturn(course);


        // when
        CourseDto result = courseController.findOne(1L).getBody();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName())
                .isEqualTo("Course 1");
    }

    @Test
    public void testFindAll() {
        // given
        CourseDto course = new CourseDto();
        course.setId(1L);
        course.setName("Course 1");

        // when
        Mockito.when(courseService.findAll()).thenReturn(List.of(course));
        List<CourseDto> result1 = courseController.findAll();

        Mockito.when(courseService.findAll()).thenReturn(List.of());
        List<CourseDto> result2 = courseController.findAll();

        // then
        assertThat(result1).isNotNull();
        assertThat(result1.size()).isEqualTo(1);
        assertThat(result1.get(0).getId()).isEqualTo(1);
        assertThat(result1.get(0).getName())
                .isEqualTo("Course 1");

        assertThat(result2).isNotNull();
        assertThat(result2.size()).isEqualTo(0);

    }

    @Test
    public void testCreateCourseBadRequests() throws InvalidRequestException {
        // given
        CourseDto course = new CourseDto();
        course.setId(1L);
        course.setName("Course 1");
        course.setStartDate(new Date(10));
        course.setEndDate(new Date(0));

        // when
        assertThrows(InvalidRequestException.class, ()->courseController.createCourse(course));
    }

    @Test
    public void testCreateCourseValid() throws InvalidRequestException {
        // given
        CourseDto course = new CourseDto();
        course.setId(1L);
        course.setName("Course 1");
        course.setStartDate(new Date(0));
        course.setEndDate(new Date(10));
        // when
        Mockito.when(courseService.create(Mockito.any())).thenReturn(course);

        CourseDto result = courseController.createCourse(course).getBody();

        // then
        assertThat(result).isNotNull();

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName())
                .isEqualTo("Course 1");
        assertThat(result.getStartDate()).isEqualTo(new Date(0));
        assertThat(result.getEndDate()).isEqualTo(new Date(10));
    }

    @Test
    public void testUpdateCourseBadRequests() throws ResourceNotFoundException {
        // given
        CourseDto course = new CourseDto();
        course.setId(1L);
        course.setName("Course 1");
        course.setStartDate(new Date(10));
        course.setEndDate(new Date(0));

        // when
        assertThrows(InvalidRequestException.class, ()->courseController.updateCourse(1L, course, "1"));
    }

    @Test
    public void testUpdateCourseValid() throws ResourceNotFoundException, InvalidRequestException {
        // given
        CourseDto course = new CourseDto();
        course.setId(1L);
        course.setName("Course 1");
        course.setStartDate(new Date(0));
        course.setEndDate(new Date(10));
        // when
        Mockito.when(courseService.update(course)).thenReturn(course);

        CourseDto result = courseController.updateCourse(1L, course, "1").getBody();

        // then
        assertThat(result).isNotNull();

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName())
                .isEqualTo("Course 1");
        assertThat(result.getStartDate()).isEqualTo(new Date(0));
        assertThat(result.getEndDate()).isEqualTo(new Date(10));
    }


    @Test
    public void testDeleteInvalidCourse() throws AlreadyStartedException {
        // given
        CourseDto course = new CourseDto();
        course.setId(1L);
        course.setName("Course 1");
        course.setStartDate(new Date(0));
        course.setEndDate(new Date(10));
        Mockito.doThrow(AlreadyStartedException.class).when(courseService).deleteById(Mockito.anyLong());

        // then
        assertThrows(AlreadyStartedException.class, ()->courseController.deleteCourse(1L));
    }

    @Test
    public void testDeleteValidCourse() throws AlreadyStartedException {
        CourseDto course = new CourseDto();
        course.setId(1L);
        course.setName("Course 1");
        course.setStartDate(new Date(0));
        course.setEndDate(new Date(10));


        // given
        ResponseEntity<String> result = courseController.deleteCourse(1L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    // TEST

    @Test
    public void testCreateTestBadRequests() throws ResourceNotFoundException {
        // given
        TestDto test = new TestDto();
        test.setId(1L);
        test.setName("Test 1");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        test.setStartDate(cal.getTime());
        cal.add(Calendar.HOUR, -1);
        test.setEndDate(cal.getTime());

        // when
        assertThrows(InvalidRequestException.class, ()->courseController.createTest(1L, test));

        // then
        Mockito.verify(testService, times(0)).create(Mockito.any(), Mockito.any());
    }

    @Test
    public void testCreateTestValidRequest() throws ResourceNotFoundException, InvalidRequestException {
        // given
        TestDto test = new TestDto();
        test.setId(1L);
        test.setName("Test 1");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        test.setStartDate(cal.getTime());
        cal.add(Calendar.HOUR, 1);
        test.setEndDate(cal.getTime());

        Mockito.when(testService.create(1L, test)).thenReturn(test);

        // when
        ResponseEntity<TestDto> result = courseController.createTest(1L, test);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        TestDto testDto = result.getBody();
        assertThat(testDto).isNotNull();
        assertThat(testDto.getId()).isEqualTo(1L);
    }


    @Test
    public void testUpdateTestBadRequests() throws ResourceNotFoundException, InvalidRequestException {
        // given
        TestDto test = new TestDto();
        test.setId(1L);
        test.setName("Test 1");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        test.setStartDate(cal.getTime());
        cal.add(Calendar.HOUR, -1);
        test.setEndDate(cal.getTime());

        // when
        assertThrows(InvalidRequestException.class, ()->courseController.updateTest(1L, 1L, test));

        // then
        Mockito.verify(testService, times(0)).update(Mockito.any());
    }

    @Test
    public void testUpdateTestValidRequest() throws ResourceNotFoundException, InvalidRequestException {
        // given
        TestDto test = new TestDto();
        test.setId(1L);
        test.setName("Test 1");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        test.setStartDate(cal.getTime());
        cal.add(Calendar.HOUR, 1);
        test.setEndDate(cal.getTime());

        Mockito.when(testService.create(1L, test)).thenReturn(test);

        // when
        ResponseEntity<TestDto> result = courseController.createTest(1L, test);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        TestDto testDto = result.getBody();
        assertThat(testDto).isNotNull();
        assertThat(testDto.getId()).isEqualTo(1L);
    }

    @Test
    public void testFindOneTest() throws ResourceNotFoundException {
        // given
        TestDto testDto = new TestDto();
        testDto.setId(1L);
        testDto.setName("Test 1");
        Mockito.when(testService.findOne(1L, 1L)).thenReturn(testDto);


        // when
        TestDto result = courseController.findOneTest(1L, 1L).getBody();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName())
                .isEqualTo("Test 1");
    }

    @Test
    public void testFindAllTest() {

        // given
        TestDto test = new TestDto();
        test.setId(1L);
        test.setName("Test 1");

        // when
        Mockito.when(testService.findAllByCourse(1L, 0, 10)).thenReturn(new PageImpl<>(List.of(test)));
        Page<TestDto> result1 = courseController.findAllByCourse(1L, 0, 10).getBody();

        Mockito.when(testService.findAllByCourse(1L, 1, 5)).thenReturn(new PageImpl<>(List.of()));
        Page<TestDto> result2 = courseController.findAllByCourse(1L, 1, 5).getBody();

        // then
        assertThat(result1).isNotNull();
        assertThat(result1.getTotalElements()).isEqualTo(1);
        assertThat(result1.getContent().get(0).getId()).isEqualTo(1);
        assertThat(result1.getContent().get(0).getName())
                .isEqualTo("Test 1");

        assertThat(result2).isNotNull();
        assertThat(result2.getTotalElements()).isEqualTo(0);
    }

    @Test
    public void testDeleteInvalidTest() throws AlreadyStartedException {
        // given
        TestDto testDto = new TestDto();
        testDto.setId(1L);
        testDto.setName("Test 1");
        Mockito.doThrow(AlreadyStartedException.class).when(testService).deleteById(Mockito.anyLong());

        // then
        assertThrows(AlreadyStartedException.class, () -> courseController.deleteTest(1L, 1L).getStatusCode());
    }

    @Test
    public void testDeleteValidTest() throws AlreadyStartedException {

        TestDto testDto = new TestDto();
        testDto.setId(1L);
        testDto.setName("Test 1");

        // given
        ResponseEntity<String> result = courseController.deleteTest(1L, 1L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


}