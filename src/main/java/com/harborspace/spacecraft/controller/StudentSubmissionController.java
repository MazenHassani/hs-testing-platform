package com.harborspace.spacecraft.controller;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.validation.Valid;

import com.harborspace.spacecraft.ApiHelper;
import com.harborspace.spacecraft.course.service.TestService;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.new_mail.MailRequest;
import com.harborspace.spacecraft.new_mail.MailService;
import com.harborspace.spacecraft.student_submission.StudentScore;
import com.harborspace.spacecraft.student_submission.dto.StudentSubmissionDto;
import com.harborspace.spacecraft.student_submission.service.StudentSubmissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiHelper.API_SUBMISSION_PREFIX)
@Validated
public class StudentSubmissionController {
    @Autowired
    private StudentSubmissionService studentSubmissionService;

    @Autowired
    private TestService testService;

    @PostMapping("/")
    // @PreAuthorize("hasRole('course_manager') or
    // @securityService.isParticipant(#id, authentication)")
    public String submit(@Valid @RequestBody StudentSubmissionDto studentSubmissionDto)
            throws ResourceNotFoundException {
        studentSubmissionService.create(studentSubmissionDto);
        return "Hi, it's done!! ;)";
    }

    @GetMapping("/test/{testId}")
    @PreAuthorize("hasRole('course_manager') or @securityService.isParticipant(#id, authentication)")
    public List<StudentSubmissionDto> getSubmissions(@PathVariable long testId) throws ResourceNotFoundException {
        return studentSubmissionService.getAllTestSubmissions(testId);
    }

    @GetMapping("/{taskId}")
    public List<StudentSubmissionDto> getsubmissions(@PathVariable long taskId) {
        // return new
        // ResponseEntity<>(studentSubmissionService.getStudentSubmission(taskId),
        // HttpStatus.OK);
        return studentSubmissionService.getStudentSubmission(taskId);
    }

    @PostMapping("course/{course_id}/test/{test_id}/{student_id}")
    @PreAuthorize("hasRole('course_manager') or @securityService.isParticipant(#id, authentication)")
    public String passedTest(@PathVariable("course_id") long courseId, @PathVariable("test_id") long testId,
            @PathVariable("student_id") String studentId) throws ResourceNotFoundException, AddressException, MessagingException {
        //TODO: get student ID from Authorization
        // here is the StudentScore
        //testService.getScoresByStudentId(courseId, testId, studentId);
        // edit here
        //MailService mailService = MailService.getInstance();
        //mailService.send(studentId, "Test result", "Pierre");
        return "Congratulations, you we'll send you results by mail";
    }
}