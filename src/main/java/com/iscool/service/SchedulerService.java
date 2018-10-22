package com.iscool.service;


import com.iscool.domain.SharedFile;
import com.iscool.repository.SharedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.LockModeType;
import java.io.File;
import java.sql.Date;
import java.util.List;

@Service
public class SchedulerService {
    private SharedFileRepository sharedFileRepository;

    @Autowired
    public SchedulerService(SharedFileRepository sharedFileRepository) {
        this.sharedFileRepository = sharedFileRepository;
    }

    @Scheduled(cron = "${delete_out_of_date_files_cron}")
    public void deleteOutOfDateFiles() {
        Date currentDate = new Date(System.currentTimeMillis());
        List<SharedFile> outOfDateSharedFiles = sharedFileRepository.findAllByActiveToBefore(currentDate);
        for (SharedFile outOfDateFile : outOfDateSharedFiles) {
            System.out.println("Found file: " + outOfDateFile.getFilename() + ", id = " + outOfDateFile.getId());
            deleteFile(outOfDateFile.getFilepath()); // delete from disk
            sharedFileRepository.delete(outOfDateFile); // delete from db
        }
    }

    private void deleteFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
    }
}
