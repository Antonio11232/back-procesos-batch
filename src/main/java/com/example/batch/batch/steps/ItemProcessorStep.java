package com.example.batch.batch.steps;

import com.example.batch.batch.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class ItemProcessorStep implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        log.info("Inicio STEP-PROCESSOR");

        List<Person> personList = (List<Person>) chunkContext
                .getStepContext()
                        .getStepExecution()
                                .getJobExecution().getExecutionContext()
                        .get("personList");

        assert personList != null;
        List<Person> personFinalList = personList.stream().map(persona -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            persona.setInsertionDate(formatter.format(LocalDateTime.now()));
            return persona;
       }).toList();

      chunkContext.getStepContext()
                      .getStepExecution()
                              .getJobExecution()
                                      .getExecutionContext()
                                              .put("personList",personFinalList);

        log.info("fin STEP-PROCESSOR");
        return RepeatStatus.FINISHED;
    }
}
