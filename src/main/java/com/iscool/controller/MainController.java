package com.iscool.controller;

import com.iscool.domain.SharedFile;
import com.iscool.repository.SharedFileRepository;
import com.iscool.service.SharedFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Date;

@Controller
public class MainController {

    private final SharedFileRepository sharedFileRepository;
    private SharedFileService sharedFileService;

    @Autowired
    public MainController(SharedFileRepository sharedFileRepository, SharedFileService sharedFileService) {
        this.sharedFileRepository = sharedFileRepository;
        this.sharedFileService = sharedFileService;
    }

    @RequestMapping({"/", "/index"})
    public String main() {
        return "index";
    }

    @PostMapping("/load_file")
    public String shareFile(@RequestParam("shared_file") MultipartFile multipartFile,
                            @RequestParam("fromUser") String from,
                            @RequestParam("toEmail") String emailTo,
                            @RequestParam("activeTo") Date activeToDate,
                            RedirectAttributes redirectAttributes) throws IOException {
        sharedFileService.saveFile(multipartFile, from, emailTo, activeToDate);
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



}
