package com.example.batch.batch.steps;

import com.example.batch.batch.entity.Person;
import com.example.batch.batch.persistence.IPersonDAO;
import com.example.batch.batch.service.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.List;

@Slf4j
public class ItemWriterStep implements Tasklet {

    private final IPersonService personService;

    public ItemWriterStep(IPersonService personService) {
        this.personService = personService;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        log.info("Inicio STEP-WRITER");
        List<Person> listadoPersonas =(List<Person>) chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext()
                .get("personList");

        personService.saveAll(listadoPersonas);
        log.info("Fin STEP-WRITER");
        return RepeatStatus.FINISHED;
    }
}
