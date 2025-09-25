package com.example.batch.batch.steps;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
public class ItemProcessorStep implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        log.info("Inicio STEP-PROCESSOR");

        List<Map<String, Object>> personsListProcesor = (List<Map<String, Object>>) chunkContext
                .getStepContext()
                .getStepExecution()
                .getJobExecution().getExecutionContext()
                .get("personsListReader");

        if (personsListProcesor == null) {
            throw new RuntimeException("No se encontro 'personsListReader' en el executionContext");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        for (Map<String, Object> personMap : personsListProcesor) {
            personMap.put("insertionDate", formatter.format(LocalDateTime.now()));
        }

        chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .put("personFinalList", personsListProcesor);

        log.info("fin STEP-PROCESSOR");
        return RepeatStatus.FINISHED;
    }
}
