package com.harborspace.spacecraft.controller;

import com.harborspace.spacecraft.exception.ResourceNotFoundException;
import com.harborspace.spacecraft.invitation.response.InvitationResponse;
import com.harborspace.spacecraft.invitation.service.InvitationService;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class InvitationControllerTest {

    @InjectMocks
    private InvitationController invitationController;

    @Mock
    private InvitationService invitationService;

    @Test
    public void findInvitationFail() throws ResourceNotFoundException {
        // given
        Mockito.doThrow(ResourceNotFoundException.class).when(invitationService).findOne("1");

        // when
        assertThrows(ResourceNotFoundException.class,() -> invitationController.findOne("1"));

        // then
    }


    @Test
    public void findInvitationSucceed() throws ResourceNotFoundException {
        // given
        InvitationResponse response = new InvitationResponse().setId(1L).setCourseId(1L).setUserId("1");
        Mockito.when(invitationService.findOne("1")).thenReturn(response);

        // when
        ResponseEntity<InvitationResponse> result = invitationController.findOne("1");

        // then
        assertNotNull(result);
        assertEquals("1", Objects.requireNonNull(result.getBody()).getUserId());
    }
}
