package com.example.batch.batch.steps;

import com.example.batch.batch.entity.Person;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.ResourceLoader;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ItemReaderStep implements Tasklet {


    private final ResourceLoader resourceLoader;

    public ItemReaderStep(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {


        log.info("-------> Inicio de STEP-READER");
        // classpath = resources
        Reader reader = new FileReader(resourceLoader.getResource("classpath:files/destino/persons.csv").getFile());

        CSVParser csvParser = new CSVParserBuilder()
                .withSeparator(',')
                .build();

        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withCSVParser(csvParser)
                .withSkipLines(1)
                .build();

        List<Person> personList = new ArrayList<>();
        String[] registroActual;

        while ((registroActual = csvReader.readNext()) != null) {
            Person person = new Person();
            person.setName(registroActual[0]);
            person.setLastName(registroActual[1]);
            person.setAge(Integer.parseInt(registroActual[2]));

            personList.add(person);
        }

        csvReader.close();

        log.info("-------> Fin de STEP-READER");
        chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .put("personList",personList);

        return RepeatStatus.FINISHED;
    }
}
