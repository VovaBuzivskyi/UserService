package school.faang.user_service.filter.goal_invitation.impl;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import school.faang.user_service.dto.goal.GoalInvitationFilterDto;
import school.faang.user_service.entity.goal.GoalInvitation;
import school.faang.user_service.filter.goal_invitation.GoalInvitationFilterSpec;

@Component
public class InviterIdFilter implements GoalInvitationFilterSpec {
    @Override
    public Specification<GoalInvitation> toSpecification(GoalInvitationFilterDto filterDto) {
        return (root, query, criteriaBuilder) -> {
            if (filterDto != null && filterDto.getInviterId() != null) {
                criteriaBuilder.equal(root.get("inviter").get("id"), filterDto.getInviterId());
            }
            return criteriaBuilder.conjunction();
        };
    }
}
