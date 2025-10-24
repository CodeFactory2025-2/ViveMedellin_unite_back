package com.communityapp.group.repository;

import com.communityapp.group.model.GroupPost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GroupPostRepository extends JpaRepository<GroupPost, Long> {
    List<GroupPost> findByGroupIdOrderByCreatedAtDesc(Long groupId);
    
}
