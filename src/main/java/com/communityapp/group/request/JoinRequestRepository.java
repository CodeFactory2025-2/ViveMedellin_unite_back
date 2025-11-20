package com.communityapp.group.request;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long>  {
    boolean existsByUserIdAndGroupIdAndStatus(Long userId, Long groupId, JoinRequestStatus status);
    Optional<JoinRequest> findByUserIdAndGroupIdAndStatus(Long userId, Long groupId, JoinRequestStatus status);
    
}
