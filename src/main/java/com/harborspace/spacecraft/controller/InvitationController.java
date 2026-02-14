package com.harborspace.spacecraft.controller;


import com.harborspace.spacecraft.ApiHelper;
import com.harborspace.spacecraft.core.UserFilter;
import com.harborspace.spacecraft.course.dto.CourseDto;
import com.harborspace.spacecraft.exception.AlreadyInvitedException;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.invitation.model.Invitation;
import com.harborspace.spacecraft.invitation.request.InvitationRequest;
import com.harborspace.spacecraft.invitation.response.InvitationResponse;
import com.harborspace.spacecraft.invitation.service.InvitationService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(ApiHelper.API_INVITATION_PREFIX)
@Validated
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    @GetMapping("/")
    public ResponseEntity<Page<InvitationResponse>> findAllByUser(
            @RequestAttribute(UserFilter.USER_ID) String userId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return new ResponseEntity<>(invitationService.findAllByUser(userId, page, size), HttpStatus.OK);
    }

    @GetMapping("/course/{course_id}")
    @PreAuthorize("hasRole('course_manager') or @securityService.isParticipant(#courseId, authentication)")
    public ResponseEntity<Page<InvitationResponse>> findAllByCourse(
            @PathVariable("course_id") Long courseId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return new ResponseEntity<>(invitationService.findAllByCourse(courseId, page, size),
                HttpStatus.OK);
    }

    @GetMapping("/course/{course_id}/user")
    public ResponseEntity<Invitation.InvitationType> findOneByCourseAndUser(
            @PathVariable("course_id") Long courseId,
            @RequestAttribute(UserFilter.USER_ID) String userId) throws ResourceNotFoundException {
        Invitation.InvitationType invitationType;
        invitationType = invitationService.findOneByCourseAndUserId(courseId,userId).getType();
        return new ResponseEntity<>(invitationType, HttpStatus.OK);
    }

    @PostMapping("/")
    @PreAuthorize(
            "hasRole('course_manager') or @securityService.isTeacher(#invitation.courseId, authentication)")
    public ResponseEntity<List<InvitationResponse>> invite(@Valid @RequestBody InvitationRequest invitation) throws ResourceNotFoundException {
        return new ResponseEntity<>(invitationService.create(invitation), HttpStatus.CREATED);
    }



    @GetMapping("/{invitationCode}/course")
    public ResponseEntity<CourseDto> findCourse(@PathVariable String invitationCode) throws ResourceNotFoundException {
        return new ResponseEntity<>(invitationService.getCourse(invitationCode), HttpStatus.OK);
    }

    @GetMapping("/{invitationCode}/")
    public ResponseEntity<InvitationResponse> findOne(@PathVariable String invitationCode) throws ResourceNotFoundException {
        return new ResponseEntity<>(invitationService.findOne(invitationCode), HttpStatus.OK);
    }

    @PutMapping("/{invitationCode}/accept/")
    public ResponseEntity<InvitationResponse> acceptInvite(
            @RequestAttribute(UserFilter.USER_ID) String userId,
            @RequestAttribute(value = UserFilter.USER_NAME, required = false) String username,
            @PathVariable String invitationCode) throws AlreadyInvitedException,
            ResourceNotFoundException {
        return new ResponseEntity<>(invitationService.acceptInvite(invitationCode, userId, username),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}/")
    @PreAuthorize("hasRole('course_manager')")
    public ResponseEntity deleteInvite(@PathVariable("id") Long id) {
        invitationService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
