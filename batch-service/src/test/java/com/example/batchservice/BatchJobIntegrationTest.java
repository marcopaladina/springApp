package com.example.batchservice;

import com.example.batchservice.repository.ProdottoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
class BatchJobIntegrationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Test
    void ilJobCaricaCorrettamenteIProdottiSuH2() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        var execution = jobLauncherTestUtils.launchJob(params);

        assertThat(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(prodottoRepository.count()).isEqualTo(8);
        assertThat(prodottoRepository.existsByCodice("P001")).isTrue();
    }
}
