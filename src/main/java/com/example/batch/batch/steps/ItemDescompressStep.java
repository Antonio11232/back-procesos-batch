package com.example.batch.batch.steps;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
public class ItemDescompressStep implements Tasklet {

    private final ResourceLoader resourceLoader;

    public ItemDescompressStep(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("Inicio STEP-DESCOMPRESS");

        // 1. Obtener el recurso ZIP desde el classpath
        Resource resource = resourceLoader.getResource("classpath:files/persons.zip");

        // 2. Copiar el contenido del ZIP a un archivo temporal
        File tempZipFile = File.createTempFile("persons", ".zip");
        try (InputStream is = resource.getInputStream();
             FileOutputStream os = new FileOutputStream(tempZipFile)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }

        // 3. Crear carpeta de destino en /tmp/destino
        File destDir = new File(System.getProperty("java.io.tmpdir"), "destino");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        // 4. Descomprimir el ZIP en el directorio temporal
        try (ZipFile zipFile = new ZipFile(tempZipFile)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File outFile = new File(destDir, entry.getName());

                if (entry.isDirectory()) {
                    outFile.mkdirs();
                } else {
                    try (InputStream is = zipFile.getInputStream(entry);
                         FileOutputStream os = new FileOutputStream(outFile)) {

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = is.read(buffer)) > 0) {
                            os.write(buffer, 0, length);
                        }
                    }
                }
            }
        }

        log.info("Fin STEP-DESCOMPRESS. Archivos extraídos en: {}", destDir.getAbsolutePath());

        // Si deseas compartir la ruta de salida con otros steps:
        chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .put("outputFolder", destDir.getAbsolutePath());

        return RepeatStatus.FINISHED;
    }
}

