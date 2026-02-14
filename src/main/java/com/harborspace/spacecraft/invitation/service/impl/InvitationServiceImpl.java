package com.harborspace.spacecraft.invitation.service.impl;

import com.harborspace.spacecraft.course.dto.CourseDto;
import com.harborspace.spacecraft.course.model.Course;
import com.harborspace.spacecraft.course.repository.CourseRepository;
import com.harborspace.spacecraft.exception.AlreadyInvitedException;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.invitation.model.Invitation;
import com.harborspace.spacecraft.invitation.request.InvitationRequest;
import com.harborspace.spacecraft.invitation.response.InvitationResponse;
import com.harborspace.spacecraft.invitation.respository.InvitationRepository;
import com.harborspace.spacecraft.invitation.service.InvitationService;
import com.harborspace.spacecraft.mail.MailRequest;
import com.harborspace.spacecraft.mail.SendGridEmailService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InvitationServiceImpl implements InvitationService {
    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SendGridEmailService emailService;

    @Value("${invitation.from}")
    private String invitationFromEmail;

    @Value("${invitation.message}")
    private String invitationMessage;

    @Value("${invitation.subject}")
    private String invitationSubject;

    @Value("${email.address}")
    private String address;

    @Value("${email.port}")
    private String port;

    private void sendInvitation(String to, String courseName, Invitation invitation) {
        String portInURL = "";
        if (port.isEmpty()) {
            portInURL = "";
        }
        else {
            portInURL = ":" + port;
        }
        String URL = "http://" + address + portInURL + "/invite/" + invitation.getInvitationCode();
        MailRequest mailRequest =
                MailRequest.builder()
                        .to(to)
                        .from(invitationFromEmail)
                        .message(invitationMessage)
                        .subject(String.format("Invitation to %s", courseName))
                        .build();
        emailService.sendEmailWithSendGrid(mailRequest, courseName, invitation.getType(), URL);
    }

    @Override
    public List<InvitationResponse> create(InvitationRequest request) throws ResourceNotFoundException {
        //validate the request
        // 1. the course exists
        Optional<Course> retrievedCourseOpt = courseRepository.findById(request.getCourseId());
        if (retrievedCourseOpt.isEmpty()) {
            throw new ResourceNotFoundException("Course");
        }
        Course retrievedCourse = retrievedCourseOpt.get();

        List<Invitation> invitations = request.getEmails().stream()
                .map(email -> {
                    Invitation invitation = new Invitation();
                    invitation.setCourse(retrievedCourse);
                    invitation.setType(request.getType());
                    sendInvitation(email, invitation.getCourse().getName(), invitation);
                    return invitation;
                }).collect(Collectors.toList());

        List<InvitationResponse> responses = new ArrayList<>();
        invitationRepository.saveAll(invitations)
                .forEach(courseInvitation ->
                        responses.add(modelMapper.map(courseInvitation, InvitationResponse.class)));
        return responses;
    }

    public InvitationResponse findOne(long id) throws ResourceNotFoundException {
        Optional<Invitation> invitationOpt = invitationRepository.findById(id);
        if (invitationOpt.isEmpty()) throw new ResourceNotFoundException("Invitation");
        return modelMapper.map(invitationOpt.get(), InvitationResponse.class);
    }

    @Override
    public InvitationResponse findOne(String code) throws ResourceNotFoundException {
        Optional<Invitation> invitationOpt = invitationRepository.findByInvitationCode(code);
        if (invitationOpt.isEmpty()) throw new ResourceNotFoundException("Invitation");
        return modelMapper.map(invitationOpt.get(), InvitationResponse.class);
    }

    @Override
    public Page<InvitationResponse> findAllByUser(String userId, int page, int pageSize) {
        if (page < 0)
            page = 0;
        if (pageSize < 5)
            pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Invitation> invitations = invitationRepository.findAllByUserId(userId, pageable);
        return invitations.map(invitation -> modelMapper.map(invitation, InvitationResponse.class));
    }


    @Override
    public Page<InvitationResponse> findAllByCourse(long courseId, int page, int pageSize) {
        if (page < 0)
            page = 0;
        if (pageSize < 5)
            pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Invitation> invitations = invitationRepository.findAllByCourseIdAndUserIdIsNotNull(courseId, pageable);
        return invitations.map(invitation -> modelMapper.map(invitation, InvitationResponse.class));
    }

    @Override
    public InvitationResponse findOneByCourseAndUserId(long courseId, String userId) throws ResourceNotFoundException {

        Optional<Invitation> invitationOpt = invitationRepository.findByCourseIdAndUserId(courseId, userId);
        if (invitationOpt.isEmpty()) throw new ResourceNotFoundException("Invitation");
        return modelMapper.map(invitationOpt.get(), InvitationResponse.class);
    }

    @Override
    public InvitationResponse update(Long id, InvitationRequest invitationRequest) throws ResourceNotFoundException {
        Optional<Invitation> retrievedInvitation = invitationRepository.findById(id);
        if (retrievedInvitation.isEmpty()) {
            throw new ResourceNotFoundException("Invitation");
        }
        //check that the update changes at least one property
        Invitation updatedInvitation = retrievedInvitation.get();
        InvitationRequest currentInvitation = modelMapper.map(updatedInvitation, InvitationRequest.class);
        if (!currentInvitation.equals(invitationRequest)) {
            BeanUtils.copyProperties(invitationRequest, updatedInvitation);
            updatedInvitation = invitationRepository.save(updatedInvitation);
        }
        return modelMapper.map(updatedInvitation, InvitationResponse.class);
    }

    @Override
    public InvitationResponse acceptInvite(String invitationCode, String userId, String username) throws AlreadyInvitedException,
            ResourceNotFoundException {
        Optional<Invitation> retrievedInvitation = invitationRepository.findByInvitationCode(invitationCode);
        if (retrievedInvitation.isEmpty()) {
            throw new ResourceNotFoundException("Invitation");
        }

        Invitation updatedInvitation = retrievedInvitation.get();
        Long courseId = updatedInvitation.getCourse().getId();
        if (userId != null && invitationRepository.existsByCourseIdAndUserId(courseId, userId)) {
            throw new AlreadyInvitedException(courseId, userId);
        }

        updatedInvitation.setUserId(userId);
        updatedInvitation.setUsername(username);
        updatedInvitation = invitationRepository.save(updatedInvitation);
        return modelMapper.map(updatedInvitation, InvitationResponse.class);
    }

    @Override
    public void deleteById(long id) {
        if (invitationRepository.existsById(id)) {
            invitationRepository.deleteById(id);
        }
    }

    @Override
    public CourseDto getCourse(String invitationCode) throws ResourceNotFoundException {
        Optional<Invitation> invitationOpt = invitationRepository.findByInvitationCode(invitationCode);
        if (invitationOpt.isEmpty()) throw new ResourceNotFoundException("Invitation");
        Invitation invitation = invitationOpt.get();
        return modelMapper.map(invitation.getCourse(), CourseDto.class);
    }
}
