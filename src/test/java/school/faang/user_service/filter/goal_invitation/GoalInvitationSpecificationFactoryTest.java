package school.faang.user_service.filter.goal_invitation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import school.faang.user_service.dto.goal.GoalInvitationFilterDto;
import school.faang.user_service.entity.goal.GoalInvitation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalInvitationSpecificationFactoryTest {

    @Mock
    private GoalInvitationFilterSpec filter1;

    @Mock
    private GoalInvitationFilterSpec filter2;

    @InjectMocks
    private GoalInvitationSpecificationFactory factory;

    @BeforeEach
    void setUp() {
        factory = new GoalInvitationSpecificationFactory(List.of(filter1, filter2));
    }

    @Test
    void buildSpecification_ShouldCombineAllFilters() {
        GoalInvitationFilterDto filterDto = new GoalInvitationFilterDto();
        Specification<GoalInvitation> spec1 = mock(Specification.class);
        Specification<GoalInvitation> spec2 = mock(Specification.class);

        when(filter1.toSpecification(filterDto)).thenReturn(spec1);
        when(filter2.toSpecification(filterDto)).thenReturn(spec2);

        Specification<GoalInvitation> result = factory.buildSpecification(filterDto);

        assertNotNull(result);
        verify(filter1).toSpecification(filterDto);
        verify(filter2).toSpecification(filterDto);
    }
}