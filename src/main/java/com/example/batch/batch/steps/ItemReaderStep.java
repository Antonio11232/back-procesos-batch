package com.example.batch.batch.steps;

import com.example.batch.batch.entity.Person;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.ResourceLoader;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ItemReaderStep implements Tasklet,StepExecutionListener{


    private final ResourceLoader resourceLoader;
    List<Map<String,Object>> personsListReader;

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



        String[] registroActual;

        while ((registroActual = csvReader.readNext()) != null) {

            Map<String,Object> personMap = new HashMap<>();
            personMap.put("name",registroActual[0]);
            personMap.put("lastName",registroActual[1]);
            personMap.put("age",Integer.parseInt(registroActual[2]));

            personsListReader.add(personMap);
        }

        csvReader.close();

        log.info("-------> Fin de STEP-READER");

        return RepeatStatus.FINISHED;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        personsListReader = new ArrayList<>();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
         stepExecution.getJobExecution().getExecutionContext().put("personsListReader",personsListReader);
         return ExitStatus.COMPLETED;
    }
}
