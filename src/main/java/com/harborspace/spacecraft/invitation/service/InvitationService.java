package com.harborspace.spacecraft.invitation.service;

import com.harborspace.spacecraft.course.dto.CourseDto;
import com.harborspace.spacecraft.exception.AlreadyInvitedException;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.invitation.request.InvitationRequest;
import com.harborspace.spacecraft.invitation.response.InvitationResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface InvitationService {
    List<InvitationResponse> create(InvitationRequest request) throws ResourceNotFoundException;

    InvitationResponse findOne(String code) throws ResourceNotFoundException;

    Page<InvitationResponse> findAllByUser(String userId, int page, int pageSize);

    Page<InvitationResponse> findAllByCourse(long courseId, int page, int pageSize);

    InvitationResponse findOneByCourseAndUserId(long courseId, String userId) throws ResourceNotFoundException;

    InvitationResponse update(Long id, InvitationRequest invitationRequest) throws ResourceNotFoundException;

    InvitationResponse acceptInvite(String invitationCode, String userId, String username) throws AlreadyInvitedException,
            ResourceNotFoundException;

    void deleteById(long id);

    CourseDto getCourse(String invitationCode) throws ResourceNotFoundException;
}
