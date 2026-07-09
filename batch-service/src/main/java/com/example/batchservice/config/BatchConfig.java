package com.example.batchservice.config;

import com.example.batchservice.dto.ProdottoCsv;
import com.example.batchservice.model.Prodotto;
import com.example.batchservice.processor.ProdottoItemProcessor;
import com.example.batchservice.repository.ProdottoRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Configurazione del job batch: legge un file CSV di prodotti,
 * lo valida/trasforma e lo scrive nel database H2.
 *
 * Flusso: FlatFileItemReader -> ProdottoItemProcessor -> RepositoryItemWriter
 */
@Configuration
public class BatchConfig {

    private static final int CHUNK_SIZE = 10;
    private static final int SKIP_LIMIT = 50;

    @Bean
    public FlatFileItemReader<ProdottoCsv> reader(
            @Value("${batch.input.file}") Resource resource) {

        BeanWrapperFieldSetMapper<ProdottoCsv> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(ProdottoCsv.class);

        return new FlatFileItemReaderBuilder<ProdottoCsv>()
                .name("prodottoItemReader")
                .resource(resource)
                .encoding("UTF-8")
                .linesToSkip(1) // salta l'header del CSV
                .delimited()
                .delimiter(",")
                .names("codice", "nome", "prezzo", "quantita")
                .fieldSetMapper(fieldSetMapper)
                .strict(true)
                .build();
    }

    @Bean
    public ProdottoItemProcessor processor() {
        return new ProdottoItemProcessor();
    }

    @Bean
    public RepositoryItemWriter<Prodotto> writer(ProdottoRepository repository) {
        RepositoryItemWriter<Prodotto> writer = new RepositoryItemWriter<>();
        writer.setRepository(repository);
        writer.setMethodName("save");
        return writer;
    }

    /**
     * Policy di skip: scarta le righe non valide (ValidationException)
     * senza far fallire l'intero job, fino a un massimo di SKIP_LIMIT righe.
     */
    @Bean
    public SkipPolicy skipPolicy() {
        return (Throwable t, long skipCount) ->
                t instanceof ValidationException && skipCount < SKIP_LIMIT;
    }

    @Bean
    public Step importProdottiStep(JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager,
                                    FlatFileItemReader<ProdottoCsv> reader,
                                    ProdottoItemProcessor processor,
                                    RepositoryItemWriter<Prodotto> writer,
                                    SkipPolicy skipPolicy) {

        return new StepBuilder("importProdottiStep", jobRepository)
                .<ProdottoCsv, Prodotto>chunk(CHUNK_SIZE, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipPolicy(skipPolicy)
                .build();
    }

    @Bean
    public Job importProdottiJob(JobRepository jobRepository, Step importProdottiStep) {
        return new JobBuilder("importProdottiJob", jobRepository)
                .start(importProdottiStep)
                .build();
    }
}
