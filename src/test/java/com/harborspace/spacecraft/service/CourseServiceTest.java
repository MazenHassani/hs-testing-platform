package com.harborspace.spacecraft.service;


import com.harborspace.spacecraft.course.dto.CourseDto;
import com.harborspace.spacecraft.course.model.Course;
import com.harborspace.spacecraft.course.repository.CourseRepository;
import com.harborspace.spacecraft.course.service.impl.CourseServiceImpl;
import com.harborspace.spacecraft.exception.AlreadyStartedException;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private CourseServiceImpl courseService;


    @Test
    public void testFindOneSuccess() throws ResourceNotFoundException {
        // given
        Course course = new Course();
        course.setId(1L);
        course.setName("Course 1");
        Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // when
        CourseDto courseDto = courseService.findOne(1L);

        // then
        assert courseDto!=null;
        assertThat(courseDto.getId()).isEqualTo(1L);
        assertThat(courseDto.getName())
                .isEqualTo("Course 1");
    }

    @Test
    public void testFindOneFail() throws ResourceNotFoundException {
        // given
        Mockito.when(courseRepository.findById(2L)).thenReturn(Optional.empty());

        // when
        assertThrows(ResourceNotFoundException.class, ()->courseService.findOne(2L));
    }

    @Test
    public void testFindAll()
    {
        // given
        List<Course> allCourses = new ArrayList<>();
        for(long i = 1; i <= 29; i++)
        {
            Course course = new Course();
            course.setId(i);
            course.setName("Course " + i);
            allCourses.add(course);
        }

        Mockito.when(courseRepository.findAll()).then(invocationOnMock -> allCourses);

        // when
        List<CourseDto> result = courseService.findAll();

        // then
        assertThat(result.size()).isEqualTo(allCourses.size());
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getName())
                .isEqualTo("Course 1");
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getName())
                .isEqualTo("Course 2");
    }

    @Test
    public void testCreate()
    {
        // given

        Mockito.when(courseRepository.save(Mockito.any(Course.class)))
                .then(invocationOnMock -> ((Course)invocationOnMock.getArguments()[0]).setId(1L));

        // when
        CourseDto courseDto = new CourseDto();
        courseDto.setName("Course 1");
        Calendar cal = Calendar.getInstance();
        cal.set(2020, Calendar.JUNE, 1);
        courseDto.setStartDate(cal.getTime());
        cal.set(2020, Calendar.JUNE, 22);
        courseDto.setEndDate(cal.getTime());
        CourseDto createdCourse = courseService.create(courseDto);

        // then
        assertThat(createdCourse.getId()).isNotNull();
        assertThat(createdCourse.getName())
                .isEqualTo("Course 1");
        assertThat(createdCourse.getStartDate()).isInSameDayAs("2020-06-01");
        assertThat(createdCourse.getEndDate()).isInSameDayAs("2020-06-22");
    }

    @Test
    public void testUpdateSuccess() throws ResourceNotFoundException {
        // given
        Course previousCourse = new Course().setId(1L).setName("Course 1");
        CourseDto course = new CourseDto().setId(1L).setName("Course 1 UPDATED");
        Mockito.when(courseRepository.findById(1L)).thenAnswer(invocationOnMock -> Optional.of(previousCourse));
        Mockito.when(courseRepository.save(Mockito.any(Course.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        // when
        CourseDto updatedCourseDto = courseService.update(course);

        // then
        assertThat(updatedCourseDto).isNotNull();
        assertThat(updatedCourseDto.getId()).isNotNull();
        assertThat(updatedCourseDto.getId()).isEqualTo(1L);
        assertThat(updatedCourseDto.getName()).isEqualTo("Course 1 UPDATED");
    }

    @Test
    public void testDeleteSuccess() throws AlreadyStartedException {
        // given
        Course course = new Course().setId(1L).setName("Course 1");
        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.MARCH, 1);
        course.setStartDate(cal.getTime());
        cal.set(2025, Calendar.MARCH, 22);
        course.setEndDate(cal.getTime());
        Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // when
        courseService.deleteById(1L);

        // then
        Mockito.verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAlreadyStartedCourse()
    {
        // given
        Course course = new Course().setId(1L).setName("Course 1");
        Calendar cal = Calendar.getInstance();
        cal.set(2020, Calendar.MARCH, 1);
                course.setStartDate(cal.getTime());
        cal.set(2020, Calendar.MARCH, 22);
        course.setEndDate(cal.getTime());
        Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // when
        assertThrows(AlreadyStartedException.class, () -> courseService.deleteById(1L));

        // then
    }

}
