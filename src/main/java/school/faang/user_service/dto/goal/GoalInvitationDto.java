package school.faang.user_service.dto.goal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import school.faang.user_service.entity.RequestStatus;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoalInvitationDto {

    private Long goalInvitationId;

    @NotNull(message = "Invitation should have inviter Id")
    private Long inviterId;

    @NotNull(message = "Invitation should have invited Id")
    private Long invitedId;

    @NotNull(message = "Invitation should have goal Id")
    private Long goalId;

    private RequestStatus status;
}
