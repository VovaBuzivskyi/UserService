package school.faang.user_service.validator.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import school.faang.user_service.exception.EntityNotFoundException;
import school.faang.user_service.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidator userValidator;

    @Test
    public void validateUserExistenceThrowExceptionTest() {
        boolean isExist = false;

        assertThrows(EntityNotFoundException.class,
                () -> userValidator.validateUserExistence(isExist));
    }

    @Test
    public void validateUserExistenceDoesNotThrowExceptionTest() {
        boolean isExist = true;

        assertDoesNotThrow(() -> userValidator.validateUserExistence(isExist));
    }

    @Test
    public void validateUserExistenceByIdTest() {
        long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);

        assertDoesNotThrow(() -> userValidator.validateUserExistence(userId));
    }

    @Test
    public void validateUserExistenceByIdThrowsExceptionTest() {
        long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> userValidator.validateUserExistence(userId));
    }
}

