package school.faang.user_service.filter.goal_invitation.impl;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import school.faang.user_service.dto.goal.GoalInvitationFilterDto;
import school.faang.user_service.entity.goal.GoalInvitation;
import school.faang.user_service.filter.goal_invitation.GoalInvitationFilterSpec;

@Component
public class RequestStatusFilter implements GoalInvitationFilterSpec {

    @Override
    public Specification<GoalInvitation> toSpecification(GoalInvitationFilterDto filterDto) {
        return (root, query, criteriaBuilder) -> {
            if (filterDto != null && filterDto.getRequestStatus() != null) {
                return criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("status")),
                        filterDto.getRequestStatus().toString().toLowerCase());
            }
            return criteriaBuilder.conjunction();
        };
    }
}
