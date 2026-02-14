package com.harborspace.spacecraft.service;

import com.harborspace.spacecraft.course.model.Course;
import com.harborspace.spacecraft.course.repository.CourseRepository;
import com.harborspace.spacecraft.exception.AlreadyInvitedException;
import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.invitation.model.Invitation;
import com.harborspace.spacecraft.invitation.model.Invitation.InvitationType;
import com.harborspace.spacecraft.invitation.request.InvitationRequest;
import com.harborspace.spacecraft.invitation.response.InvitationResponse;
import com.harborspace.spacecraft.invitation.respository.InvitationRepository;
import com.harborspace.spacecraft.invitation.service.impl.InvitationServiceImpl;
import com.harborspace.spacecraft.mail.SendGridEmailService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class InvitationServiceTest {
    @Mock
    InvitationRepository invitationRepository;

    @Mock
    CourseRepository courseRepository;

    @Mock
    SendGridEmailService emailService;

    @Spy
    ModelMapper modelMapper;

    @InjectMocks
    InvitationServiceImpl invitationService;

    @BeforeEach
    public void initializeProperties() {
        ReflectionTestUtils.setField(invitationService, "invitationSubject", "Invitation to %s");
        ReflectionTestUtils.setField(invitationService, "port", "8080");
        ReflectionTestUtils.setField(invitationService, "invitationFromEmail", "space.craft@harbour.space");
        ReflectionTestUtils.setField(invitationService, "invitationMessage", "You are invited to %s with link %s");
    }


    @Test
    public void testCreateInvitation() throws ResourceNotFoundException {
        //given
        Course course = new Course().setId(1L).setName("Course 1");
        UUID uuid = UUID.randomUUID();
        Mockito.when(invitationRepository.saveAll(Mockito.anyList()))
                .then(invocationOnMock -> ((List<Invitation>) invocationOnMock.getArguments()[0]).stream()
                        .map(courseInvitation -> courseInvitation
                                .setId(1L).setInvitationCode(uuid.toString()))
                        .collect(Collectors.toList()));
        Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        //when
        InvitationRequest request = new InvitationRequest();
        request.setCourseId(1L);
        request.setEmails(List.of("a@b.c"));
        List<InvitationResponse> invitationDto = invitationService.create(request);

        //then
        assertEquals(invitationDto.size(), 1);
        assertNotNull(invitationDto.get(0).getId());
        assertEquals(invitationDto.get(0).getCourseId(), 1L);
        assertNull(invitationDto.get(0).getUserId());
        assertEquals(invitationDto.get(0).getInvitationCode(), uuid.toString());
    }

    @Test
    public void testFindAllByUser() {
        //given
        List<Invitation> invitations = getInvitations();
        Mockito.when(invitationRepository.findAllByUserId(Mockito.anyString(), Mockito.any(Pageable.class)))
                .then(getInvitationPageByUser(invitations));

        //when
        Page<InvitationResponse> firstUserInvitations = invitationService.findAllByUser("KEE", 0, 5);
        Page<InvitationResponse> secondUserInvitations = invitationService.findAllByUser("John", 0, 5);

        //then
        assertThat(firstUserInvitations.getTotalElements()).isEqualTo(6);
        assertThat(firstUserInvitations.getNumberOfElements()).isEqualTo(5);
        assertThat(secondUserInvitations.getTotalElements()).isEqualTo(4);
        assertThat(secondUserInvitations.getNumberOfElements()).isEqualTo(4);

        assertThat(firstUserInvitations.getContent().stream().allMatch(response -> response.getUserId().equals("1")));
        assertThat(firstUserInvitations.getContent().stream()
                .allMatch(response -> response.getType().equals(InvitationType.STUDENT)));
        assertThat(secondUserInvitations.getContent().stream().allMatch(response -> response.getUserId().equals("2")));
        assertThat(firstUserInvitations.getContent().stream()
                .allMatch(response -> response.getType().equals(InvitationType.TEACHER)));
    }

    @Test
    public void testFindAllByCourse() {
        //given
        List<Invitation> invitations = getInvitations();
        Mockito.when(invitationRepository.findAllByCourseIdAndUserIdIsNotNull(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .then(getInvitationPageByCourse(invitations));

        //when
        Page<InvitationResponse> firstCourseInvitations = invitationService.findAllByCourse(6L, 0, 5);
        Page<InvitationResponse> secondCourseInvitations = invitationService.findAllByCourse(15L, 0, 5);

        //then
        assertThat(firstCourseInvitations.getTotalElements()).isEqualTo(1);
        assertThat(firstCourseInvitations.getNumberOfElements()).isEqualTo(1);
        assertThat(secondCourseInvitations.getTotalElements()).isEqualTo(2);
        assertThat(secondCourseInvitations.getNumberOfElements()).isEqualTo(2);

        assertThat(firstCourseInvitations.getContent().stream()
                .allMatch(response -> response.getCourseId().equals(6L)));
        assertThat(secondCourseInvitations.getContent().stream()
                .allMatch(response -> response.getCourseId().equals(15L)));
    }

    @Test
    public void testFindByCourseAndUser() throws ResourceNotFoundException {
        //given
        Invitation invitationTeacher = new Invitation()
                .setCourse(new Course().setId(1L))
                .setId(1L)
                .setType(InvitationType.TEACHER)
                .setUserId("John");
        Invitation invitationStudent = new Invitation()
                .setCourse(new Course().setId(1L))
                .setId(2L)
                .setType(InvitationType.STUDENT)
                .setUserId("John2");

        Mockito.when(invitationRepository.findByCourseIdAndUserId(1L, "John"))
                .thenReturn(Optional.of(invitationTeacher));
        Mockito.when(invitationRepository.findByCourseIdAndUserId(1L, "John2"))
                .thenReturn(Optional.of(invitationStudent));
        Mockito.when(invitationRepository.findByCourseIdAndUserId(1L, "John3"))
                .thenReturn(Optional.empty());

        //when
        InvitationType firstResponse = invitationService.findOneByCourseAndUserId(1L, "John").getType();
        InvitationType secondResponse = invitationService.findOneByCourseAndUserId(1L, "John2").getType();
        assertThrows(ResourceNotFoundException.class, () -> invitationService.findOneByCourseAndUserId(1L, "John3"));

        //then
        assertThat(firstResponse).isEqualTo(InvitationType.TEACHER);
        assertThat(secondResponse).isEqualTo(InvitationType.STUDENT);
        Mockito.verify(invitationRepository, times(3)).findByCourseIdAndUserId(Mockito.anyLong(), Mockito.anyString());
    }

    @Test
    public void testUpdateSuccess() throws ResourceNotFoundException {
        // given
        Invitation invitaitonToUpdate = new Invitation()
                .setId(1L)
                .setUserId("2")
                .setCourse(new Course().setId(3L))
                .setType(InvitationType.STUDENT);
        InvitationRequest invitationRequest = new InvitationRequest()
                .setCourseId(3L)
                .setType(InvitationType.TEACHER);

        Mockito.when(invitationRepository.findById(1L)).thenAnswer(invocationOnMock -> Optional.of(invitaitonToUpdate));
        Mockito.when(invitationRepository.save(Mockito.any(Invitation.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        // when
        InvitationResponse invitationResponse = invitationService.update(1L, invitationRequest);

        // then
        assertThat(invitationResponse).isNotNull();
        assertThat(invitationResponse.getId()).isNotNull();
        assertThat(invitationResponse.getId()).isEqualTo(1L);
        assertThat(invitationResponse.getUserId()).isEqualTo("2");
        assertThat(invitationResponse.getCourseId()).isEqualTo(3L);
        assertThat(invitationResponse.getType()).isEqualTo(InvitationType.TEACHER);
    }

    @Test
    public void testAccpetInvite() throws AlreadyInvitedException, ResourceNotFoundException {
        // given
        UUID uuid = UUID.randomUUID();
        Invitation invitaitonToUpdate = new Invitation()
                .setId(1L)
                .setCourse(new Course().setId(3L))
                .setType(InvitationType.STUDENT)
                .setInvitationCode(uuid.toString());


        Mockito.when(invitationRepository.findByInvitationCode(uuid.toString()))
                .thenAnswer(invocationOnMock -> Optional.of(invitaitonToUpdate));
        Mockito.when(invitationRepository.save(Mockito.any(Invitation.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        // when
        InvitationResponse invitationResponse = invitationService.acceptInvite(uuid.toString(), "123", "Ibrahim");

        // then
        assertThat(invitationResponse).isNotNull();
        assertThat(invitationResponse.getId()).isEqualTo(1L);
        assertThat(invitationResponse.getUserId()).isEqualTo("123");
        assertThat(invitationResponse.getCourseId()).isEqualTo(3L);
        assertThat(invitationResponse.getType()).isEqualTo(InvitationType.STUDENT);

    }

    @Test
    public void testDelete() {
        // given

        Mockito.when(invitationRepository.existsById(1L)).thenReturn(true);

        // when
        invitationService.deleteById(1L);

        // then
        Mockito.verify(invitationRepository, times(1)).deleteById(1L);
    }

    private List<Invitation> getInvitations() {
        List<Invitation> invitations = new ArrayList<>();
        for (long i = 1L; i <= 6; i++) {
            Invitation invitation = new Invitation()
                    .setCourse(new Course().setId(3 * i))
                    .setId(i)
                    .setType(InvitationType.STUDENT)
                    .setUserId("KEE");
            invitations.add(invitation);
        }
        for (long i = 1L; i <= 4; i++) {
            Invitation invitation = new Invitation()
                    .setCourse(new Course().setId(5 * i))
                    .setId(i)
                    .setType(InvitationType.TEACHER)
                    .setUserId("John");
            invitations.add(invitation);
        }
        return invitations;
    }

    private Answer<Object> getInvitationPageByUser(List<Invitation> invitations) {
        return invocationOnMock -> {
            String userId = invocationOnMock.getArgument(0);
            Pageable pageable = invocationOnMock.getArgument(1);
            int start = pageable.getPageNumber() * pageable.getPageSize();
            int end = start + pageable.getPageSize();
            List<Invitation> userInvitations =
                    invitations.stream().filter(invitation -> invitation.getUserId().equals(userId))
                            .collect(Collectors.toList());
            if (end > userInvitations.size()) end = userInvitations.size();
            if (start > end) start = end;
            return new PageImpl<>(userInvitations.subList(start, end), pageable, userInvitations.size());
        };
    }

    private Answer<Object> getInvitationPageByCourse(List<Invitation> invitations) {
        return invocationOnMock -> {
            Long courseId = invocationOnMock.getArgument(0);
            Pageable pageable = invocationOnMock.getArgument(1);
            int start = pageable.getPageNumber() * pageable.getPageSize();
            int end = start + pageable.getPageSize();
            List<Invitation> courseInvitations =
                    invitations.stream().filter(invitation -> invitation.getCourse().getId().equals(courseId))
                            .collect(Collectors.toList());
            if (end > courseInvitations.size()) end = courseInvitations.size();
            if (start > end) start = end;
            return new PageImpl<>(courseInvitations.subList(start, end), pageable, courseInvitations.size());
        };
    }
}
