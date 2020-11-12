package br.com.marcelo.processador.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

@Service
@Slf4j
public class ProcuraArquivos {

    @Value("${br.com.marcelo.processador.entrada}")
    String entrada;

    @Value("${br.com.marcelo.processador.saida}")
    String saida;

    @Autowired
    CarregaArquivo carregaArquivo;

    @Async("procuraArquivoTaskExec")
    public void buscaDadosDisco() {
        Path dirtoWatch = Path.of(entrada);
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            var key = dirtoWatch.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            while (true) {
                WatchKey watchKey = watchService.take();
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    var arquivo = (Path) event.context();
                    if (event.kind().equals(ENTRY_CREATE)){
                        log.info("Encontrado arquivo a processar: {} ", event.context());
                        Path arquivoProcessar = Paths.get(dirtoWatch.toString(), arquivo.toString());
                        prontoParaProcessar(arquivoProcessar);
                        carregaArquivo.buscaDadosDisco(arquivoProcessar);
                    }
                    watchKey.reset();
                }
                watchKey.reset();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void prontoParaProcessar(Path path) throws IOException, InterruptedException {
        long tamanho = 0;
        File file = new File(String.valueOf(path));
        while (tamanho != file.length()){
            tamanho = file.length();
            Thread.sleep(500);
            file = new File(String.valueOf(path));
        }
    }

    @Bean("procuraArquivoTaskExec")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("procura-arquivo-");
        return executor;
    }
}
