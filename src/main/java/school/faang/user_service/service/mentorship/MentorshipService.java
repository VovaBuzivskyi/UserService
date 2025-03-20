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
        log.info("Mentors found: {}, for mentee with id: {}", mentors, menteeId);
        return userMapper.toDtoList(mentors);
    }
}
