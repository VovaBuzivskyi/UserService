package school.faang.user_service.service.mentorship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.faang.user_service.dto.mentorship.MentorshipRequestCreationDto;
import school.faang.user_service.dto.mentorship.MentorshipRequestDto;
import school.faang.user_service.dto.mentorship.RejectionDto;
import school.faang.user_service.dto.mentorship.RequestFilterDto;
import school.faang.user_service.entity.MentorshipRequest;
import school.faang.user_service.entity.RequestStatus;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.goal.Goal;
import school.faang.user_service.filter.mentorship.RequestFilter;
import school.faang.user_service.mapper.mentorship.MentorshipRequestMapper;
import school.faang.user_service.redis.event.MentorshipAcceptedEvent;
import school.faang.user_service.redis.event.MentorshipRequestedEvent;
import school.faang.user_service.redis.publisher.MentorshipAcceptedEventPublisher;
import school.faang.user_service.redis.publisher.MentorshipRequestedEventPublisher;
import school.faang.user_service.repository.mentorship.MentorshipRequestRepository;
import school.faang.user_service.validator.mentorship.MentorshipRequestDtoValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Service
public class MentorshipRequestService {

    private final MentorshipRequestRepository requestRepository;
    private final MentorshipRequestDtoValidator requestValidator;
    private final MentorshipRequestMapper requestMapper;
    private final List<RequestFilter> requestFilters;
    private final MentorshipAcceptedEventPublisher mentorshipAcceptedEventPublisher;
    private final MentorshipRequestedEventPublisher mentorshipRequestedEventPublisher;
    private final MentorshipRequestDtoValidator mentorshipRequestDtoValidator;

    @Transactional
    public MentorshipRequestDto requestMentorship(MentorshipRequestCreationDto creationRequestDto) {
        Long requesterId = creationRequestDto.getRequesterId();
        Long receiverId = creationRequestDto.getReceiverId();
        log.info(
                "Received a mentorship request! Requester ID - {}, Receiver ID - {}.",
                requesterId,
                receiverId
        );

        requestValidator.validateCreationRequest(creationRequestDto);

        MentorshipRequest request = requestMapper.toMentorshipRequest(creationRequestDto);
        request.setStatus(RequestStatus.PENDING);
        MentorshipRequest savedRequest = requestRepository.save(request);
        log.info(
                "The mentorship request has been saved in data base! Requester ID - {}, receiver ID - {}, date of creation - {}",
                requesterId, receiverId, savedRequest.getCreatedAt()
        );

        MentorshipRequestedEvent event = MentorshipRequestedEvent.builder()
                .requesterId(requesterId)
                .receiverId(receiverId)
                .requestedAt(LocalDateTime.now())
                .build();
        mentorshipRequestedEventPublisher.publish(event);
        log.info("MentorshipRequestedEvent has been published");

        return requestMapper.toMentorshipRequestDto(savedRequest);
    }

    @Transactional
    public List<MentorshipRequestDto> getRequests(RequestFilterDto filterDto) {
        log.info("A request has been received to retrieve mentorship requests with the provided filter.");
        Stream<MentorshipRequest> requestsToFilter = requestRepository.findAll().stream();
        List<RequestFilter> filtersToApply =
                requestFilters.stream()
                        .filter(requestFilter -> requestFilter.isApplicable(filterDto))
                        .toList();

        for (RequestFilter filter : filtersToApply) {
            requestsToFilter = filter.apply(requestsToFilter, filterDto);
        }

        List<MentorshipRequest> filteredRequests = requestsToFilter.toList();

        log.info("Requests matching the given filters have been successfully found! Total requests: {}", filteredRequests.size());
        return requestMapper.toDtoList(filteredRequests);
    }

    @Transactional
    public MentorshipRequestDto acceptRequest(Long requestId) {
        log.info("Accepting mentorship request with ID: {}", requestId);
        MentorshipRequest request = requestValidator.validateAcceptRequest(requestId);

        User mentee = request.getRequester();
        User mentor = request.getReceiver();

        initializeLists(mentee);
        initializeLists(mentor);

        mentorshipRequestDtoValidator.validateMenteeHasMentorAddIfAbsents(mentee,mentor);
        mentorshipRequestDtoValidator.validateMentorHasMenteeAddIfAbsent(mentee,mentor);

        request.setStatus(RequestStatus.ACCEPTED);
        MentorshipRequest savedRequest = requestRepository.save(request);
        log.info("Successfully accepted request ID: {}", requestId);

        MentorshipAcceptedEvent event = new MentorshipAcceptedEvent(requestId, request.getRequester().getId(), request.getReceiver().getUsername());
        mentorshipAcceptedEventPublisher.publish(event);
        log.info("MentorshipAcceptedEvent with requestId: {}, requesterId: {}, receiver username: {} was sent to Redis channel",
                event.getId(), event.getRequesterId(), event.getReceiverUsername());

        return requestMapper.toMentorshipRequestDto(savedRequest);
    }

    @Transactional
    public MentorshipRequestDto rejectRequest(Long requestId, RejectionDto rejectionDto) {
        log.info("Rejecting mentorship request with ID: {}. Reason: {}", requestId, rejectionDto.getReason());
        MentorshipRequest request = requestValidator.validateRejectRequest(requestId);

        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(rejectionDto.getReason());

        MentorshipRequest savedRequest = requestRepository.save(request);
        log.info("Successfully rejected request ID: {}", requestId);
        return requestMapper.toMentorshipRequestDto(savedRequest);
    }

    @Transactional
    public void deleteMentor(User mentor) {
        List<User> mentees = mentor.getMentees();
        mentees.forEach(mentee -> {
            mentee.getMentors().remove(mentor);

            List<Goal> goals = mentee.getGoals();
            goals.forEach(goal -> {
                if (goal.getMentor().equals(mentor)) {
                    goal.setMentor(mentee);
                }
            });
            log.info("{} Goals was updated successfully", goals.size());
        });
        log.info("Mentor with id {}, was deleted for {} mentees", mentor.getId(), mentees.size());
    }

    private void initializeLists(User user) {
        if (user.getMentors() == null) {
            user.setMentors(new ArrayList<>());
        }
        if (user.getMentees() == null) {
            user.setMentees(new ArrayList<>());
        }
    }
}