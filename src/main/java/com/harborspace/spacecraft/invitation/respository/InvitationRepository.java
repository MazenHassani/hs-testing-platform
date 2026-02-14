package com.harborspace.spacecraft.invitation.respository;

import com.harborspace.spacecraft.invitation.model.Invitation;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends CrudRepository<Invitation, Long> {
    boolean existsByCourseIdAndUserIdAndType(Long courseId, String userId, Invitation.InvitationType type);
    boolean existsByCourseIdAndUserId(Long courseId, String userId);
    Page<Invitation> findAllByCourseIdAndUserIdIsNotNull(Long courseId, Pageable pageRequest);
    Page<Invitation> findAllByUserId(String userId, Pageable pageRequest);
    Optional<Invitation> findByInvitationCode(String invitationCode);
    Optional<Invitation> findByCourseIdAndUserId(Long courseId, String userId);
}
