package com.ajemi.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // 🚀 Hit kadd-khdem b l-Async f Notifications
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO) // ✅ Had l-sṭar houwa l-feks!
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}