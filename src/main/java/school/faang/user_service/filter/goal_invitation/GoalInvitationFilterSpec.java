package school.faang.user_service.filter.goal_invitation;

import org.springframework.data.jpa.domain.Specification;
import school.faang.user_service.dto.goal.GoalInvitationFilterDto;
import school.faang.user_service.entity.goal.GoalInvitation;

public interface GoalInvitationFilterSpec {
    Specification<GoalInvitation> toSpecification(GoalInvitationFilterDto filterDto);
}
