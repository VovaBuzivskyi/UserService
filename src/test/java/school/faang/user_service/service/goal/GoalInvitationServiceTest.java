package school.faang.user_service.service.goal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import school.faang.user_service.dto.goal.GoalInvitationDto;
import school.faang.user_service.dto.goal.GoalInvitationFilterDto;
import school.faang.user_service.entity.RequestStatus;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.goal.Goal;
import school.faang.user_service.entity.goal.GoalInvitation;
import school.faang.user_service.exception.EntityNotFoundException;
import school.faang.user_service.filter.goal_invitation.GoalInvitationSpecificationFactory;
import school.faang.user_service.mapper.goal.GoalInvitationMapper;
import school.faang.user_service.mapper.goal.GoalInvitationMapperImpl;
import school.faang.user_service.repository.goal.GoalInvitationRepository;
import school.faang.user_service.service.user.UserService;
import school.faang.user_service.validator.goal.GoalInvitationValidator;
import school.faang.user_service.validator.goal.GoalValidator;
import school.faang.user_service.validator.user.UserValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalInvitationServiceTest {

    @Mock
    private GoalInvitationRepository goalInvitationRepository;

    @Spy
    private GoalInvitationMapper goalInvitationMapper = new GoalInvitationMapperImpl();

    @Mock
    private GoalInvitationValidator goalInvitationValidator;

    @Mock
    private UserService userService;

    @Mock
    private GoalService goalService;

    @Mock
    private UserValidator userValidator;

    @Mock
    private GoalValidator goalValidator;

    @Mock
    private GoalInvitationSpecificationFactory specificationFactory;

    @InjectMocks
    private GoalInvitationService goalInvitationService;

    @Test
    void sendGoalInvitationTest() {
        ArgumentCaptor<GoalInvitation> captor = ArgumentCaptor.forClass(GoalInvitation.class);
        long goalId = 1L;
        long inviterId = 2L;
        long invitedId = 3L;

        GoalInvitationDto dto = GoalInvitationDto.builder()
                .goalId(goalId)
                .inviterId(inviterId)
                .invitedId(invitedId)
                .build();

        User inviter = User.builder().build();
        User invited = User.builder().build();
        Goal goal = Goal.builder().build();

        GoalInvitation goalInvitation = GoalInvitation.builder()
                .id(10L)
                .invited(invited)
                .inviter(inviter)
                .goal(goal)
                .build();

        when(userService.getUserEntity(inviterId)).thenReturn(inviter);
        when(userService.getUserEntity(invitedId)).thenReturn(invited);
        when(goalService.getGoalById(goalId)).thenReturn(goal);
        when(goalInvitationRepository.save(any())).thenReturn(goalInvitation);

        goalInvitationService.sendGoalInvitation(dto);

        verify(userValidator).validateUserExistence(inviterId);
        verify(userValidator).validateUserExistence(invitedId);
        verify(goalValidator).validateGoalExistence(goalId);
        verify(goalInvitationMapper).toEntity(dto);
        verify(goalInvitationRepository).save(captor.capture());

        GoalInvitation result = captor.getValue();
        assertNotNull(result);
        assertEquals(inviter, result.getInviter());
        assertEquals(invited, result.getInvited());
        assertEquals(goal, result.getGoal());
        assertEquals(RequestStatus.PENDING, result.getStatus());
    }

    @Test
    void acceptGoalInvitationThrowsExceptionTest() {
        long goalInvitationId = 1L;
        long invitedId = 2L;

        when(goalInvitationRepository.findById(goalInvitationId)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> goalInvitationService.acceptGoalInvitation(goalInvitationId, invitedId));

        verify(userValidator).validateUserExistence(invitedId);
    }

    @Test
    void acceptGoalInvitationTest() {
        long goalInvitationId = 1L;
        long invitedId = 2L;
        GoalInvitation invitation = GoalInvitation.builder()
                .id(goalInvitationId)
                .build();

        when(goalInvitationRepository.findById(goalInvitationId)).thenReturn(Optional.of(invitation));

        goalInvitationService.acceptGoalInvitation(goalInvitationId, invitedId);

        verify(userValidator).validateUserExistence(invitedId);
        verify(goalInvitationValidator).isUserInvitedToGoal(invitation, invitedId);
        verify(goalInvitationRepository).save(invitation);

        assertEquals(RequestStatus.ACCEPTED, invitation.getStatus());
    }

    @Test
    void rejectGoalInvitationThrowsExceptionTest() {
        long goalInvitationId = 1L;
        long invitedId = 2L;

        when(goalInvitationRepository.findById(goalInvitationId)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> goalInvitationService.rejectGoalInvitation(goalInvitationId, invitedId));

        verify(userValidator).validateUserExistence(invitedId);
    }

    @Test
    void rejectGoalInvitationTest() {
        long goalInvitationId = 1L;
        long invitedId = 2L;
        GoalInvitation invitation = GoalInvitation.builder()
                .id(goalInvitationId)
                .build();

        when(goalInvitationRepository.findById(goalInvitationId)).thenReturn(Optional.of(invitation));

        goalInvitationService.rejectGoalInvitation(goalInvitationId, invitedId);

        verify(userValidator).validateUserExistence(invitedId);
        verify(goalInvitationValidator).isUserInvitedToGoal(invitation, invitedId);
        verify(goalInvitationRepository).save(invitation);

        assertEquals(RequestStatus.REJECTED, invitation.getStatus());
    }

    @Test
    void getAllGaolInvitation() {
        User inviter = User.builder()
                .id(1L)
                .build();
        User invited = User.builder()
                .id(2L)
                .build();
        Goal goal = Goal.builder()
                .id(10L)
                .build();

        GoalInvitation goalInvitation = GoalInvitation.builder()
                .id(10L)
                .invited(invited)
                .inviter(inviter)
                .goal(goal)
                .status(RequestStatus.ACCEPTED)
                .build();

        GoalInvitationFilterDto filters = GoalInvitationFilterDto.builder().build();
        Specification<GoalInvitation> specification = mock(Specification.class);
        List<GoalInvitation> invitations = new ArrayList<>(List.of(goalInvitation));

        when(specificationFactory.buildSpecification(filters)).thenReturn(specification);
        when(goalInvitationRepository.findAll(specification)).thenReturn(invitations);

        List<GoalInvitationDto> result = goalInvitationService.getAllGaolInvitation(filters);

        verify(goalInvitationMapper).toDtoList(invitations);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}