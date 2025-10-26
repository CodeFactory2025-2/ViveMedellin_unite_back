package com.communityapp.group.repository;

import com.communityapp.group.model.GroupPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupPostRepository extends JpaRepository<GroupPost, Long> {
    List<GroupPost> findByGroupIdOrderByCreatedAtDesc(Long groupId);

     // ✅ Corrección: los nombres del parámetro deben coincidir (keyword = keyword)
    @Query("""
        SELECT p FROM GroupPost p
        WHERE p.group.id = :groupId
        AND (
            LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(p.author.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        ORDER BY p.createdAt DESC
    """)
    List<GroupPost> searchPostsByContent(
        @Param("groupId") Long groupId,
        @Param("keyword") String keyword
    );
}

    

