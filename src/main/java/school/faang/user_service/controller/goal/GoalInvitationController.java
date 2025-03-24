package school.faang.user_service.controller.goal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school.faang.user_service.dto.goal.GoalInvitationDto;
import school.faang.user_service.service.goal.GoalInvitationService;

import java.util.List;

@RestController
@RequestMapping("/goal-invitations")
@RequiredArgsConstructor
public class GoalInvitationController {

    private final GoalInvitationService goalInvitationService;

    @PostMapping
    public void sendGaolInvitation(@RequestBody GoalInvitationDto goalInvitationDto){
        goalInvitationService.sendGoalInvitation(goalInvitationDto);
    }

    @PatchMapping("/{goalInvitationId}")
    public void acceptGaolInvitation(@PathVariable Long goalInvitationId){
        goalInvitationService.acceptGoalInvitation(goalInvitationId);
    }

    @PatchMapping("/{goalInvitationId}")
    public void rejectGaolInvitation(@PathVariable Long goalInvitationId){
        goalInvitationService.rejectGoalInvitaion(goalInvitationId);
    }

    public List<GoalInvitationDto> getAllGaolInvitation(@RequestParam Long invitedUserId){
        return goalInvitationService.getAllGaolInvitation(invitedUserId);
    }
}
