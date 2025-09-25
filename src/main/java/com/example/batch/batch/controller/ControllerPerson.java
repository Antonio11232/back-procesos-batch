package com.example.batch.batch.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class ControllerPerson {

    private final JobLauncher jobLauncher;
    private final Job job;

    public ControllerPerson(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @PostMapping("/cargarArchivo")
    public ResponseEntity<?> receiveFile(@RequestParam(name = "file") MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();

        try {
            Path path = Paths.get("src" + File.separator + "main" + File.separator + "resources" + File.separator + "files" + File.separator + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            log.info("**********Inicio del proceso BATCH******************");
            assert fileName != null;
            JobParameters jobParameters = new JobParametersBuilder()
                    .addDate("fecha", new Date())
                    .addString("fileName",fileName)
                    .toJobParameters();

            jobLauncher.run(job,jobParameters);



            Map<String,String> response = new HashMap<>();
            response.put("archivo",fileName);
            response.put("estado","recibido");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al iniciar el proceso BATCH", e.getMessage());
            throw new RuntimeException();
        }
    }

    @GetMapping("/hola")
    public ResponseEntity<?> hola(){
        return ResponseEntity.ok("SALUDOS");
    }
}
