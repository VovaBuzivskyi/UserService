package school.faang.user_service.filter.goal_invitation.impl;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import school.faang.user_service.dto.goal.GoalInvitationFilterDto;
import school.faang.user_service.entity.goal.GoalInvitation;
import school.faang.user_service.filter.goal_invitation.GoalInvitationFilterSpec;

@Component
public class InviterNameFilter implements GoalInvitationFilterSpec {
    @Override
    public Specification<GoalInvitation> toSpecification(GoalInvitationFilterDto filterDto) {
        return ((root, query, criteriaBuilder) -> {
            if (filterDto != null && filterDto.getInviterNamePattern() != null) {
                return criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("inviter").get("username")),
                        "%" + filterDto.getInviterNamePattern() + "%");
            }
            return criteriaBuilder.conjunction();
        });
    }
}
