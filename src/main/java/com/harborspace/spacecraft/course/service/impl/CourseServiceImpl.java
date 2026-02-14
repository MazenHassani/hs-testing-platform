package com.harborspace.spacecraft.course.service.impl;

import com.harborspace.spacecraft.course.dto.CourseDto;
import com.harborspace.spacecraft.course.model.Course;
import com.harborspace.spacecraft.course.repository.CourseRepository;
import com.harborspace.spacecraft.course.service.CourseService;
import com.harborspace.spacecraft.exception.AlreadyStartedException;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CourseDto findOne(Long id) throws ResourceNotFoundException {
        return courseRepository.findById(id)
                .map(course -> modelMapper.map(course, CourseDto.class))
                .orElseThrow(() -> new ResourceNotFoundException("Class"));
    }

    @Override
    public List<CourseDto> findAll() {
        List<Course> courses = (List<Course>) courseRepository.findAll();
        return courses.stream().map(course -> modelMapper.map(course, CourseDto.class)).collect(Collectors.toList());
    }


    @Override
    public CourseDto create(CourseDto courseDto) {
        Course course = modelMapper.map(courseDto, Course.class);
        course.setId(null);
        Course savedCourse = courseRepository.save(course);
        return modelMapper.map(savedCourse, CourseDto.class);
    }

    @Override
    public CourseDto update(CourseDto courseDto) throws ResourceNotFoundException {
        Optional<Course> retrievedCourse = courseRepository.findById(courseDto.getId());
        if (retrievedCourse.isEmpty()) {
            throw new ResourceNotFoundException("Course");
        }
        Course updatedCourse = retrievedCourse.get();
        BeanUtils.copyProperties(courseDto, updatedCourse);
        updatedCourse = courseRepository.save(updatedCourse);
        return modelMapper.map(updatedCourse, CourseDto.class);
    }

    @Override
    public void deleteById(Long id) throws AlreadyStartedException {
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            if (course.getStartDate() == null || new Date().after(course.getStartDate())) {
                throw new AlreadyStartedException("Course");
            }
            courseRepository.deleteById(id);
        }
    }


}
