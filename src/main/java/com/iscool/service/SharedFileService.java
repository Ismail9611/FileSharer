package com.iscool.service;

import com.iscool.domain.SharedFile;
import com.iscool.repository.SharedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.UUID;


@Service
public class SharedFileService {

    @Value("${shared_files_storage}")
    private String filesPath;

    private SharedFileRepository sharedFileRepository;
    private MailService mailService;

    @Autowired
    public SharedFileService(SharedFileRepository sharedFileRepository, MailService mailService){
        this.sharedFileRepository = sharedFileRepository;
        this.mailService = mailService;
    }

    public void saveFile(MultipartFile multipartFile, String from,
                         String emailTo, Date activeTo) throws IOException {
        File storedFile = multipartToFile(multipartFile); // storing the file on disk
        String notEmptyFrom = from.equals("") ? "UnknownSender" : from;

        SharedFile sharedFile = new SharedFile();
        sharedFile.setFilename(multipartFile.getOriginalFilename());
        sharedFile.setFilepath(storedFile.getAbsolutePath());
        sharedFile.setFrom(from);
        sharedFile.setTo(emailTo);
        sharedFile.setActiveTo(activeTo);
        sharedFile.setUrl("http://localhost:8080/sharedFile/" + UUID.randomUUID().toString() + "/"
                + multipartFile.getOriginalFilename());


        new Thread(() -> {
            sharedFileRepository.save(sharedFile);
            String subject = notEmptyFrom + " have sent you a file..";
            String messageToReceiver = "Hello, " + notEmptyFrom + " have sent you file. You can access file by link: \n"
                    + sharedFile.getUrl() + "\n" + "The link is active until: " + sharedFile.getActiveTo()
                    + ". After this date, the file will be deleted.";

            mailService.send(emailTo, subject, messageToReceiver);
        }, "File_Saving_Thread").start();


    }

    private File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
        if (multipart == null) throw new NullPointerException();
        File convFile = new File(filesPath + "/" + multipart.getOriginalFilename());
        multipart.transferTo(convFile);
        return convFile;
    }

}
