package school.faang.user_service.controller.goal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.faang.user_service.config.context.UserContext;
import school.faang.user_service.dto.goal.GoalInvitationDto;
import school.faang.user_service.dto.goal.GoalInvitationFilterDto;
import school.faang.user_service.service.goal.GoalInvitationService;

import java.util.List;

@RestController
@RequestMapping("/goal-invitations")
@RequiredArgsConstructor
public class GoalInvitationController {

    private final GoalInvitationService goalInvitationService;
    private final UserContext userContext;

    @PostMapping
    public void sendGaolInvitation(@RequestBody GoalInvitationDto goalInvitationDto) {
        goalInvitationService.sendGoalInvitation(goalInvitationDto);
    }

    @PatchMapping("/{goalInvitationId}")
    public void acceptGaolInvitation(@PathVariable long goalInvitationId) {
        long invitedId = userContext.getUserId();
        goalInvitationService.acceptGoalInvitation(goalInvitationId, invitedId);
    }

    @PatchMapping("/reject/{goalInvitationId}")
    public void rejectGaolInvitation(@PathVariable Long goalInvitationId) {
        long invitedId = userContext.getUserId();
        goalInvitationService.rejectGoalInvitation(goalInvitationId, invitedId);
    }

    @PostMapping("/filters")
    public List<GoalInvitationDto> getAllGaolInvitation(@RequestBody GoalInvitationFilterDto filters) {
        return goalInvitationService.getAllGaolInvitation(filters);
    }
}
