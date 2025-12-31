package com.ajemi.backend.service;

import com.ajemi.backend.dto.AdminReportResponseDTO;
import com.ajemi.backend.entity.Report;
import com.ajemi.backend.repository.ReportRepository;
import com.ajemi.backend.repository.UserRepository;
import com.ajemi.backend.entity.User;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.lang.NonNull;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

     public void reportUser(String reporterUsername, Long reportedUserId, String reason) {

        User reporter = userRepository.findByUsername(reporterUsername)
                .orElseThrow(() -> new RuntimeException("Reporter not found"));

        User reported = userRepository.findById(@NonNull reportedUserId)
                .orElseThrow(() -> new RuntimeException("Reported user not found"));

        // 🛑 ما يمكنش report راسك
        if (reporter.getId().equals(reported.getId())) {
            throw new RuntimeException("You cannot report yourself");
        }

        Report report = new Report();
        report.setReporter(reporter);
        report.setReportedUser(reported);
        report.setReason(reason);

        reportRepository.save(report);
    }
   public List<AdminReportResponseDTO> getAllReports() {
    return reportRepository.findAll()
            .stream()
            .map(r -> new AdminReportResponseDTO(
                    r.getId(),
                    r.getReason(),
                    r.getReporter().getUsername(),
                    r.getReportedUser().getUsername(),
                    r.getCreatedAt()
            ))
            .toList();
    }

}
