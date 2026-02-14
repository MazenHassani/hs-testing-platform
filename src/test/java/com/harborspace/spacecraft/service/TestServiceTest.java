package com.harborspace.spacecraft.service;


import com.harborspace.spacecraft.course.dto.TestDto;
import com.harborspace.spacecraft.course.model.Course;
import com.harborspace.spacecraft.course.repository.CourseRepository;
import com.harborspace.spacecraft.course.repository.TestRepository;
import com.harborspace.spacecraft.course.service.impl.TestServiceImpl;
import com.harborspace.spacecraft.exception.AlreadyStartedException;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TestServiceTest {

    @Spy
    private TestRepository testRepository;

    @Spy
    private CourseRepository courseRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private TestServiceImpl testService;


    @Test
    public void testFindOneSuccess() throws ResourceNotFoundException {
        // given
        var test = new com.harborspace.spacecraft.course.model.Test();
        test.setId(1L).setName("Test 1").setCourse(new Course().setId(1L));
        Mockito.when(testRepository.findById(1L)).thenReturn(Optional.of(test));

        // when
        TestDto result = testService.findOne(1L, 1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("Test 1");
    }

    @Test
    public void testFindOneFail() throws ResourceNotFoundException {
        // given
        var test = new com.harborspace.spacecraft.course.model.Test();
        test.setId(1L).setName("Test 1").setCourse(new Course().setId(1L));
        Mockito.when(testRepository.findById(1L)).thenReturn(Optional.of(test));

        // when
        assertThrows(ResourceNotFoundException.class, () -> testService.findOne(2L, 1L));
        assertThrows(ResourceNotFoundException.class, () -> testService.findOne(1L, 2L));

        // then
    }

    @Test
    public void testFindAll() {
        // given
        List<com.harborspace.spacecraft.course.model.Test> tests = new java.util.ArrayList<>();
        for (long i = 0; i < 25; i++) {
            var test = new com.harborspace.spacecraft.course.model.Test();
            test.setId(i).setName("Mock Test");
            tests.add(test);
            testRepository.save(test);
        }
        Mockito.when(testRepository.findAll(Mockito.any(Pageable.class))).then(invocationOnMock -> {
            Pageable pageable = invocationOnMock.getArgument(0);
            int start = pageable.getPageNumber() * pageable.getPageSize();
            int end = start + pageable.getPageSize();
            if (end > tests.size()) end = tests.size();
            if (start > end) start = end;
            return new PageImpl<>(tests.subList(start, end), pageable, tests.size());
        });

        // when
        Page<TestDto> result = testService.findAll(1, 5);

        // then
        assertThat(result.getTotalElements()).isEqualTo(25);
        assertThat(result.getNumberOfElements()).isEqualTo(5);
        assertThat(result.getContent().get(0).getId()).isEqualTo(5L);
        assertThat(result.getContent().get(2).getId()).isEqualTo(7L);
    }

    @Test
    public void testFindAllByCourse() {
        // given
        List<com.harborspace.spacecraft.course.model.Test> tests = new java.util.ArrayList<>();
        for (long i = 0; i < 50; i++) {
            var test = new com.harborspace.spacecraft.course.model.Test();
            test.setId(i).setName("Mock Test").setCourse(new Course().setId(i / 10));
            tests.add(test);
            testRepository.save(test);
        }
        Mockito.when(testRepository.findAllByCourseId(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .then(invocationOnMock -> {
                    Long courseId = invocationOnMock.getArgument(0);
                    Pageable pageable = invocationOnMock.getArgument(1);
                    int start = pageable.getPageNumber() * pageable.getPageSize();
                    int end = start + pageable.getPageSize();
                    List<com.harborspace.spacecraft.course.model.Test> inCourse =
                            tests.stream().filter(test -> test.getCourse().getId().equals(courseId))
                                    .collect(Collectors.toList());
                    if (end > inCourse.size()) end = inCourse.size();
                    if (start > end) start = end;
                    return new PageImpl<>(inCourse.subList(start, end), pageable, inCourse.size());
                });

        // when
        Page<TestDto> result0 = testService.findAllByCourse(0L, 1, 5);
        Page<TestDto> result2 = testService.findAllByCourse(2L, 1, 5);

        // then
        assertThat(result0.getTotalElements()).isEqualTo(10);
        assertThat(result0.getNumberOfElements()).isEqualTo(5);
        assertThat(result0.getContent().get(0).getId()).isEqualTo(5L);
        assertThat(result0.getContent().get(2).getId()).isEqualTo(7L);

        assertThat(result2.getTotalElements()).isEqualTo(10);
        assertThat(result2.getNumberOfElements()).isEqualTo(5);
        assertThat(result2.getContent().get(0).getId()).isEqualTo(25L);
        assertThat(result2.getContent().get(2).getId()).isEqualTo(27L);
    }

    @Test
    public void testCreatTestFail() {
        // given
        TestDto test = new TestDto();
        test.setId(1L).setName("Test 1");

        // when and then
        assertThrows(ResourceNotFoundException.class, () -> testService.create(1L, test));
    }

    @Test
    public void testCreatTestSuccess() throws ResourceNotFoundException {
        // given
        TestDto test = new TestDto();
        test.setName("Test 1");
        Course course = new Course().setId(1L).setName("Course 1");
        Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        Mockito.when(testRepository.save(Mockito.any()))
                .then(invocationOnMock -> ((com.harborspace.spacecraft.course.model.Test) invocationOnMock.getArgument(0))
                        .setId(1L));

        // when
        TestDto testDto = testService.create(1L, test);

        // then
        assert testDto != null;
        ArgumentCaptor<com.harborspace.spacecraft.course.model.Test> argument =
                ArgumentCaptor.forClass(com.harborspace.spacecraft.course.model.Test.class);
        Mockito.verify(testRepository, Mockito.times(1)).save(argument.capture());
        assertEquals(course.getName(), argument.getValue().getCourse().getName());
    }

    @Test
    public void testUpdateSuccess() throws ResourceNotFoundException {
        // given
        Course course = new Course().setId(1L).setName("Course 1");
        var previousTest =
                new com.harborspace.spacecraft.course.model.Test().setId(1L).setName("Test 1").setCourse(course);
        TestDto test = new TestDto().setId(1L).setName("Test 1 UPDATED");
        Mockito.when(testRepository.findById(1L)).thenAnswer(invocationOnMock -> Optional.of(previousTest));
        Mockito.when(testRepository.save(Mockito.any(com.harborspace.spacecraft.course.model.Test.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        // when
        TestDto updatedTestDto = testService.update(test);

        // then
        assertThat(updatedTestDto).isNotNull();
        assertThat(updatedTestDto.getId()).isEqualTo(1);
        assertThat(updatedTestDto.getName()).isEqualTo("Test 1 UPDATED");
    }

    @Test
    public void testUpdateFail() throws ResourceNotFoundException {
        // given
        TestDto test = new TestDto().setId(1L).setName("Test 1 UPDATED");

        // when
        assertThrows(ResourceNotFoundException.class, () -> testService.update(test));

        // then
        Mockito.verify(testRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    public void testDeleteSuccess() throws AlreadyStartedException {
        // given
        var test = new com.harborspace.spacecraft.course.model.Test().setId(1L).setName("Course 1");
        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.MARCH, 1);
        test.setStartDate(cal.getTime());
        cal.set(2025, Calendar.MARCH, 22);
        test.setEndDate(cal.getTime());
        Mockito.when(testRepository.findById(1L)).thenReturn(Optional.of(test));

        // when
        testService.deleteById(1L);

        // then
        Mockito.verify(testRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAlreadyStarted() {
        // given
        var test = new com.harborspace.spacecraft.course.model.Test().setId(1L).setName("Course 1");
        Calendar cal = Calendar.getInstance();
        cal.set(2020, Calendar.MARCH, 1);
        test.setStartDate(cal.getTime());
        cal.set(2020, Calendar.MARCH, 22);
        test.setEndDate(cal.getTime());
        Mockito.when(testRepository.findById(1L)).thenReturn(Optional.of(test));

        // when
        assertThrows(AlreadyStartedException.class, () -> testService.deleteById(1L));

        // then
    }
}
