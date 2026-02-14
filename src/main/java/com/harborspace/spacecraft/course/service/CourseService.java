package com.harborspace.spacecraft.course.service;

import com.harborspace.spacecraft.course.dto.CourseDto;
import com.harborspace.spacecraft.exception.AlreadyStartedException;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import java.util.List;

public interface CourseService {
    CourseDto findOne(Long id) throws ResourceNotFoundException;

    List<CourseDto> findAll();

    CourseDto create(CourseDto courseDto);

    CourseDto update(CourseDto courseDto) throws ResourceNotFoundException;

    void deleteById(Long id) throws AlreadyStartedException;
}
