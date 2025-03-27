package school.faang.user_service.filter.goal_invitation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import school.faang.user_service.dto.goal.GoalInvitationFilterDto;
import school.faang.user_service.entity.goal.GoalInvitation;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GoalInvitationSpecificationFactory {

    private final List<GoalInvitationFilterSpec> filters;

    public Specification<GoalInvitation> buildSpecification(GoalInvitationFilterDto filterDto) {
        Specification<GoalInvitation> specification = Specification.where(null);
        for (GoalInvitationFilterSpec filter : filters) {
            specification = specification.and(filter.toSpecification(filterDto));
        }
        return specification;
    }
}
