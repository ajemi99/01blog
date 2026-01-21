package com.ajemi.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajemi.backend.entity.Report;
import com.ajemi.backend.entity.User;

public interface ReportRepository extends JpaRepository<Report, Long> {
    void deleteAllByReporterOrReportedUser(User reporter, User reported);
    void deleteByPostId(Long postId);
    @Query("SELECT r FROM Report r " +
       "JOIN FETCH r.reporter " +
       "JOIN FETCH r.reportedUser u " +
       "JOIN FETCH u.role " +
       "ORDER BY r.createdAt DESC")
    List<Report> findAllByOrderByCreatedAtDesc();

}
