package com.ajemi.backend.repository;

import java.util.List;
import com.ajemi.backend.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ajemi.backend.entity.User;

public interface FollowRepository extends JpaRepository<Subscription, Long>{
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
        @Query("""
        SELECT s.following.id
        FROM Subscription s
        WHERE s.follower.id = :currentUserId
    """)
    List<Long> findFollowingIds(@Param("currentUserId") Long currentUserId);
    
    @Query("SELECT s.follower FROM Subscription s WHERE s.following = :user")
    List<User> findFollowersByUser(@Param("user") User user);
    void deleteAllByFollowerOrFollowing(User follower, User following);
}
