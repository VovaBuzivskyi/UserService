package school.faang.user_service.filter.goal_invitation.impl;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import school.faang.user_service.dto.goal.GoalInvitationFilterDto;
import school.faang.user_service.entity.goal.GoalInvitation;
import school.faang.user_service.filter.goal_invitation.GoalInvitationFilterSpec;

@Component
public class InvitedIdFilter implements GoalInvitationFilterSpec {

    @Override
    public Specification<GoalInvitation> toSpecification(GoalInvitationFilterDto filterDto) {
        return (root, query, criteriaBuilder) -> {
            if (filterDto != null && filterDto.getInvitedId() != null) {
                criteriaBuilder.equal(root.get("invited").get("id"), filterDto.getInvitedId());
            }
            return criteriaBuilder.conjunction();
        };
    }
}
