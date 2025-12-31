package com.ajemi.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajemi.backend.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
    
}
