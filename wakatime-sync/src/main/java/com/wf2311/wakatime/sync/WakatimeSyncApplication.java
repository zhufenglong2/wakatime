package com.wf2311.wakatime.sync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WakatimeSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(WakatimeSyncApplication.class, args);
    }

}

