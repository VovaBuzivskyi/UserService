package school.faang.user_service.validator.goal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import school.faang.user_service.entity.goal.GoalInvitation;

@Component
@RequiredArgsConstructor
public class GoalInvitationValidator {

    public void isUserInvitedToGoal(GoalInvitation invitation, long userId){
       long invitedId = invitation.getInvited().getId();
       if(invitedId != userId){
           throw new IllegalArgumentException("Current user isn't invited person of this goal invitation");
       }
    }
}
