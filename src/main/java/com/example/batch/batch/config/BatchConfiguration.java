package com.example.batch.batch.config;

import com.example.batch.batch.service.IPersonService;
import com.example.batch.batch.steps.ItemDescompressStep;
import com.example.batch.batch.steps.ItemProcessorStep;
import com.example.batch.batch.steps.ItemReaderStep;
import com.example.batch.batch.steps.ItemWriterStep;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    public final JobBuilderFactory jobBuilderFactory;
    public final StepBuilderFactory stepBuilderFactory;

    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }


    @Bean
    @JobScope
    public ItemDescompressStep itemDescompressStep(ResourceLoader resourceLoader) {
        return new ItemDescompressStep(resourceLoader);
    }

    @Bean
    @JobScope
    public ItemReaderStep itemReaderStep(ResourceLoader resourceLoader) {
        return new ItemReaderStep(resourceLoader);
    }

    @Bean
    @JobScope
    public ItemProcessorStep itemProcessorStep() {
        return new ItemProcessorStep();
    }

    @Bean
    @JobScope
    public ItemWriterStep itemWriterStep(IPersonService personService) {
        return new ItemWriterStep(personService);
    }

    @Bean
    public Step descompressFileStep(ItemDescompressStep itemDescompressStep){
        return stepBuilderFactory.get("itemDescompressStep")
                .tasklet(itemDescompressStep)
                .build();
    }

    @Bean
    public Step readFileStep(ItemReaderStep itemReaderStep){
        return stepBuilderFactory.get("itemReaderStep")
                .tasklet(itemReaderStep)
                .build();
    }

    @Bean
    public Step processorFileStep(){
        return stepBuilderFactory.get("itemProcessorStep")
                .tasklet(itemProcessorStep())
                .build();
    }

    @Bean
    public Step writeFileStep(ItemWriterStep itemWriterStep){
        return stepBuilderFactory.get("itemWriterStep")
                .tasklet(itemWriterStep)
                .build();
    }

    @Bean
    public Job readCSVJob(Step descompressFileStep,Step readFileStep,Step writeFileStep){
        return jobBuilderFactory.get("readCSVJob")
                .start(descompressFileStep)
                .next(readFileStep)
                .next(processorFileStep())
                .next(writeFileStep)
                .build();
    }

}
