package school.faang.user_service.repository.mentorship;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import school.faang.user_service.entity.User;

import java.util.List;

@Repository
public interface MentorshipRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN u.mentees m WHERE m.id = :userId")
    List<User> findMentorsByMenteeId(@Param("userId") Long userId);

    @Query("SELECT u FROM User u JOIN u.mentors m WHERE m.id = :userId")
    List<User> findMenteesByMentorId(@Param("userId") Long userId);
}
