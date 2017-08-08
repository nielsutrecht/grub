package com.nibado.project.grub.backup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class BackupScheduler {
    private static final String FILENAME_FORMAT = "grub-backup-%s.sql.gz";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmm");
    private final JdbcTemplate jdbcTemplate;
    private final File backupDirectory;

    @Autowired
    public BackupScheduler(final JdbcTemplate jdbcTemplate, @Value("${backup.directory}") final String backupDirectory) {
        this.jdbcTemplate = jdbcTemplate;
        this.backupDirectory = new File(backupDirectory);

        log.info("Backup directory: {}", this.backupDirectory.getAbsolutePath());
    }

    @Scheduled(cron = "0 0 23 ? * *")
    public void backup() {
        String fileName = String.format(FILENAME_FORMAT, LocalDateTime.now().format(DATE_TIME_FORMATTER));
        File file = new File(backupDirectory, fileName);

        log.info("Backing up data to {}", file.getAbsolutePath());

        jdbcTemplate.execute(String.format("SCRIPT TO '%s' COMPRESSION GZIP", file.getAbsolutePath()));

        log.info("Backup done");
    }
}
