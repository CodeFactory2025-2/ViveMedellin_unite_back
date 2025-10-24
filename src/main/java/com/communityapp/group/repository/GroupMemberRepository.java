package com.communityapp.group.repository;


import com.communityapp.group.model.GroupMember;
import com.communityapp.user.model.User;
import com.communityapp.group.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    boolean existsByUserIdAndGroupId(Long userId, Long groupId);
    GroupMember findByUserIdAndGroupId(Long userId, Long groupId);
} 




    

