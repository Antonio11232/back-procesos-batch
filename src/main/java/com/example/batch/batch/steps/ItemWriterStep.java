package com.example.batch.batch.steps;

import com.example.batch.batch.entity.Person;
import com.example.batch.batch.service.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ItemWriterStep implements Tasklet {

    private final IPersonService personService;

    public ItemWriterStep(IPersonService personService) {
        this.personService = personService;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        log.info("Inicio STEP-WRITER");
        List<Map<String,Object>> listadoPersonasContext = (List<Map<String,Object>>) chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext()
                .get("personFinalList");

        List<Person> personasList = new ArrayList<>();
        for (Map<String,Object> person : listadoPersonasContext){
            Person personDb = new Person();
            personDb.setName((String) person.get("name"));
            personDb.setLastName((String) person.get("lastName"));
            personDb.setAge((Integer) person.get("age"));
            personDb.setInsertionDate((String) person.get("insertionDate"));
            personasList.add(personDb);
        }

        personService.saveAll(personasList);
        log.info("Fin STEP-WRITER");
        return RepeatStatus.FINISHED;
    }
}
