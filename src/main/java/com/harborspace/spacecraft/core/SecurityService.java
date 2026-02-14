package com.harborspace.spacecraft.core;

import com.harborspace.spacecraft.invitation.model.Invitation.InvitationType;
import com.harborspace.spacecraft.invitation.respository.InvitationRepository;
import com.harborspace.spacecraft.question.repository.QuestionRepository;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public boolean isTeacher(long courseId, Authentication authentication) {
        Object authDetails = authentication.getDetails();
        if(authDetails instanceof SimpleKeycloakAccount) {
            SimpleKeycloakAccount auth = (SimpleKeycloakAccount) authDetails;
            String userId = auth.getKeycloakSecurityContext().getToken().getSubject();
            return invitationRepository.existsByCourseIdAndUserIdAndType(courseId, userId, InvitationType.TEACHER);
        }
        return false;
    }

    public boolean isParticipant(long courseId, Authentication authentication) {
        Object authDetails = authentication.getDetails();
        if(authDetails instanceof SimpleKeycloakAccount) {
            SimpleKeycloakAccount auth = (SimpleKeycloakAccount) authDetails;
            String userId = auth.getKeycloakSecurityContext().getToken().getSubject();
            return invitationRepository.existsByCourseIdAndUserId(courseId, userId);
        }
        return false;
    }

    public boolean isQuestionCreator(long questionId, Authentication authentication) {
        Object authDetails = authentication.getDetails();
        if(authDetails instanceof SimpleKeycloakAccount) {
            SimpleKeycloakAccount auth = (SimpleKeycloakAccount) authDetails;
            String userId = auth.getKeycloakSecurityContext().getToken().getSubject();
            return questionRepository.existsByIdAndCreator(questionId, userId);
        }
        return false;
    }
}
