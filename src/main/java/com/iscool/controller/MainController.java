package com.iscool.controller;

import com.iscool.domain.SharedFile;
import com.iscool.repository.SharedFileRepository;
import com.iscool.service.MailService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {

    @Value("${shared_files_storage}")
    private String filesPath;

    private final SharedFileRepository sharedFileRepository;
    private final MailService mailService;

    @Autowired
    public MainController(SharedFileRepository sharedFileRepository, MailService mailService) {
        this.sharedFileRepository = sharedFileRepository;
        this.mailService = mailService;
    }


    @RequestMapping({"/", "/index"})
    public String main() {
        return "index";
    }


    @PostMapping("/load_file")
    public String shareFile(@RequestParam("shared_file") MultipartFile file,
                            @RequestParam("fromUser") String from,
                            @RequestParam("toEmail") String emailTo,
                            @RequestParam("activeTo") Date activeToDate,
                            RedirectAttributes redirectAttributes) throws IOException {
        String notEmptyFrom = from.equals("") ? "UnknownSender" : from;

        File storedFile = multipartToFile(file); // storing file on disk

        SharedFile sharedFile = new SharedFile();
        sharedFile.setFilename(file.getOriginalFilename());
        sharedFile.setFilepath(storedFile.getAbsolutePath());
        sharedFile.setFrom(from);
        sharedFile.setTo(emailTo);
        sharedFile.setActiveTo(activeToDate);
        sharedFile.setUrl("http://localhost:8080/sharedFile/" + UUID.randomUUID().toString() + "/" + file.getOriginalFilename());


        new Thread(() -> {
            sharedFileRepository.save(sharedFile);
            String subject = notEmptyFrom + " have sent you a file..";
            String messageToReceiver = "Hello, " + notEmptyFrom + " have sent you file. You can access file by link: \n"
                    + sharedFile.getUrl() + "\n" + "The link is active until: " + sharedFile.getActiveTo()
                    + ". After this date, the file will be deleted.";

            mailService.send(emailTo, subject, messageToReceiver);
        }).start();


        redirectAttributes.addFlashAttribute("message", "The file was uploaded to server");
        return "redirect:/index";
    }

    @GetMapping("/sharedFile/{link}/{filename}")
    public StreamingResponseBody downloadSharedFile(@PathVariable(name = "link") String link,
                                                    HttpServletResponse response) throws FileNotFoundException {

        SharedFile receivedFile = sharedFileRepository.findByUrlContaining(link);
        if (receivedFile == null) throw new NullPointerException();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + receivedFile.getFilename());


        // Считывает сохраненный файл (директорую берет из объекта receivedFile), и возврашает клиенту,
        // И все это проиходит в новом потоке, чтобы не блокировать основной.
        InputStream inputStream = new FileInputStream(new File(receivedFile.getFilepath()));
        return outputStream -> new Thread(() -> {
            int nRead;
            byte[] data = new byte[1024];
            try {
                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    outputStream.write(data, 0, nRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }



    private File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
        if (multipart == null) throw new NullPointerException();
        File convFile = new File(filesPath + "/" + multipart.getOriginalFilename());
        multipart.transferTo(convFile);
        return convFile;
    }


}
