package school.faang.user_service.validator.goal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import school.faang.user_service.repository.goal.GoalRepository;

@Component
@RequiredArgsConstructor
public class GoalValidator {

    private final GoalRepository goalRepository;

    public void validateGoalExistence(long goalId) {
        boolean isGoalExists = goalRepository.existsById(goalId);
        if (!isGoalExists) {
            throw new IllegalArgumentException("Goal with id " + goalId + " does not exist");
        }
    }
}
