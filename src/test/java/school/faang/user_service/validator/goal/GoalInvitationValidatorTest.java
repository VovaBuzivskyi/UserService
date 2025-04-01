package school.faang.user_service.validator.goal;

import org.junit.jupiter.api.Test;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.goal.GoalInvitation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GoalInvitationValidatorTest {

    private final GoalInvitationValidator goalInvitationValidator = new GoalInvitationValidator();

    @Test
    void isUserInvitedToGoalTest() {
        long invitedId = 10L;

        User invited = User.builder()
                .id(invitedId)
                .build();

        GoalInvitation goalInvitation = GoalInvitation.builder()
                .id(1L)
                .invited(invited)
                .build();

        assertDoesNotThrow(() -> goalInvitationValidator.isUserInvitedToGoal(goalInvitation, invitedId));
    }

    @Test
    void isUserInvitedToGoalThrowsExceptionTest() {
        long invitedId = 10L;

        User invited = User.builder()
                .id(1L)
                .build();

        GoalInvitation goalInvitation = GoalInvitation.builder()
                .id(1L)
                .invited(invited)
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> goalInvitationValidator.isUserInvitedToGoal(goalInvitation, invitedId));
    }
}