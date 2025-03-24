package school.faang.user_service.service.goal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.goal.GoalInvitationDto;
import school.faang.user_service.entity.RequestStatus;
import school.faang.user_service.entity.goal.GoalInvitation;
import school.faang.user_service.mapper.goal.GoalInvitationMapper;
import school.faang.user_service.repository.goal.GoalInvitationRepository;
import school.faang.user_service.service.user.UserService;
import school.faang.user_service.validator.goal.GoalValidator;
import school.faang.user_service.validator.user.UserValidator;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoalInvitationService {

    private final GoalInvitationRepository goalInvitationRepository;
    private final GoalInvitationMapper goalInvitationMapper;
    private final UserService userService;
    private final GoalService goalService;
    private final UserValidator userValidator;
    private final GoalValidator goalValidator;

    public void sendGoalInvitation(final GoalInvitationDto goalInvitationDto) {
        userValidator.validateUserExistence(goalInvitationDto.getInviterId());
        userValidator.validateUserExistence(goalInvitationDto.getInvitedId());
        goalValidator.validateGoalExistence(goalInvitationDto.getGoalId());

        GoalInvitation invitation = goalInvitationMapper.toEntity(goalInvitationDto);
        invitation.setInviter(userService.getUserEntity(goalInvitationDto.getInviterId()));
        invitation.setInvited(userService.getUserEntity(goalInvitationDto.getInvitedId()));
        invitation.setGoal(goalService.getGoalById(goalInvitationDto.getGoalId()));
        invitation.setStatus(RequestStatus.PENDING);

        GoalInvitation savedInvitation = goalInvitationRepository.save(invitation);

        //add notification
        log.info("Goal invitation with id: {} sent successfully", savedInvitation.getId());
    }
}
