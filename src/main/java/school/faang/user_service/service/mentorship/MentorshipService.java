package school.faang.user_service.service.mentorship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.user.UserDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.mapper.user.UserMapper;
import school.faang.user_service.repository.mentorship.MentorshipRepository;
import school.faang.user_service.validator.user.UserValidator;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentorshipService {

    private final MentorshipRepository mentorshipRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;

    public List<UserDto> getMentors(long menteeId) {
        userValidator.validateUserExistence(menteeId);
        List<User> mentors = mentorshipRepository.findMentorsByMenteeId(menteeId);
        log.info("Mentors found: {}, for mentee with id: {}", mentors.size(), menteeId);
        return userMapper.toDtoList(mentors);
    }

    public List<UserDto> getMentees(long mentorId) {
        userValidator.validateUserExistence(mentorId);
        List<User> mentees = mentorshipRepository.findMenteesByMentorId(mentorId);
        log.info("Mentees found: {}, for mentor with id: {}", mentees.size(), mentorId);
        return userMapper.toDtoList(mentees);
    }

    public void deleteMentor(long menteeId, long mentorId) {
        userValidator.validateUserExistence(menteeId);
        userValidator.validateUserExistence(mentorId);
        mentorshipRepository.deleteMentor(menteeId, mentorId);
        log.info("Mentor with id:{} deleted, for mentee with id: {} ", mentorId, menteeId);
    }

    public void deleteMentee(long mentorId, long menteeId) {
        userValidator.validateUserExistence(menteeId);
        userValidator.validateUserExistence(mentorId);
        mentorshipRepository.deleteMentee(mentorId, menteeId);
        log.info("Mentee with id:{} deleted, for mentor with id: {} ", mentorId, menteeId);
    }
}
