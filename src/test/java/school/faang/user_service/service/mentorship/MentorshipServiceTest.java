package school.faang.user_service.service.mentorship;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import school.faang.user_service.dto.user.UserDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.mapper.user.UserMapper;
import school.faang.user_service.mapper.user.UserMapperImpl;
import school.faang.user_service.repository.mentorship.MentorshipRepository;
import school.faang.user_service.validator.user.UserValidator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipServiceTest {

    @Mock
    private MentorshipRepository mentorshipRepository;

    @Mock
    private UserValidator userValidator;

    @Spy
    private UserMapper userMapper = new UserMapperImpl();

    @InjectMocks
    private MentorshipService mentorshipService;

    @Test
    void getMentorsTest() {
        long menteeId = 1L;
        User mentor = User.builder().build();
        List<User> mentors = new ArrayList<>(List.of(mentor));

        doNothing().when(userValidator).validateUserExistence(menteeId);
        when(mentorshipRepository.findMentorsByMenteeId(menteeId)).thenReturn(mentors);

        List<UserDto> result = mentorshipService.getMentors(menteeId);

        assertEquals(result.size(), 1);
        verify(userValidator).validateUserExistence(menteeId);
        verify(userMapper).toDtoList(mentors);
    }

    @Test
    void getMenteesTest() {
        long mentorId = 1L;
        User mentor = User.builder().build();
        List<User> mentees = new ArrayList<>(List.of(mentor));

        doNothing().when(userValidator).validateUserExistence(mentorId);
        when(mentorshipRepository.findMenteesByMentorId(mentorId)).thenReturn(mentees);

        List<UserDto> result = mentorshipService.getMentees(mentorId);

        assertEquals(result.size(), 1);
        verify(userValidator).validateUserExistence(mentorId);
        verify(userMapper).toDtoList(mentees);
    }

    @Test
    void deleteMentorTest() {
        long mentorId = 1L;
        long menteeId = 2L;

        doNothing().when(userValidator).validateUserExistence(mentorId);
        doNothing().when(userValidator).validateUserExistence(menteeId);

        mentorshipService.deleteMentor(menteeId, mentorId);

        verify(mentorshipRepository).deleteMentor(menteeId, mentorId);
    }

    @Test
    void deleteMenteeTest() {
        long mentorId = 1L;
        long menteeId = 2L;

        doNothing().when(userValidator).validateUserExistence(mentorId);
        doNothing().when(userValidator).validateUserExistence(menteeId);

        mentorshipService.deleteMentee(mentorId, menteeId);
        verify(mentorshipRepository).deleteMentee(mentorId, menteeId);
    }
}