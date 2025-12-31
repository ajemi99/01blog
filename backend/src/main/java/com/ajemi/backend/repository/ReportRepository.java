package com.ajemi.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajemi.backend.entity.Report;
import com.ajemi.backend.entity.User;

public interface ReportRepository extends JpaRepository<Report, Long> {
    void deleteAllByReporterOrReportedUser(User reporter, User reported);
}
