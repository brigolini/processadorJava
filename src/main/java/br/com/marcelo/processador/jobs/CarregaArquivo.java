package br.com.marcelo.processador.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class CarregaArquivo {

    @Autowired
    ProcessadorArquivo processadorArquivo;

    @Async("carregaArquivoTaskExec")
    public void buscaDadosDisco(Path arquivo) {
        List<String> linhas = new ArrayList<>();
        try (Stream<String> stream = Files.lines(arquivo, StandardCharsets.UTF_8)) {
            stream.forEach(linhas::add);
            processadorArquivo.processarArquivo(linhas,arquivo.getFileName());
        } catch (IOException e) {
            log.error("Erro ao processar arquivo {}", arquivo.toString());
        }

    }

    @Bean("carregaArquivoTaskExec")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("carga-arquivo-");
        return executor;
    }
}
