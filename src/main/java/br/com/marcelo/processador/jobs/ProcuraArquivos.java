package br.com.marcelo.processador.jobs;

import br.com.marcelo.processador.util.DirHelper;
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

    @Autowired
    DirHelper dirHelper;

    @Autowired
    CarregaArquivo carregaArquivo;

    /**
     * Utiliza o WatchService do Java para observar a criação de um novo arquivo.
     */
    @Async("procuraArquivoTaskExec")
    public void buscaDadosDisco() {
        Path dirtoWatch = dirHelper.getDirEntrada();
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

    /**
     * Testa se a cópia de um arquivo foi totalmente terminada. A forma otimizada de se fazer essa operação depende
     * do SO. Como não sabemos onde irá rodar, geramos uma forma mais simples mas que resolve, apesar de não ser eficiente
     * @param path arquivo a ser testado
     * @throws InterruptedException Erro na thread que aguarda meio segundo para ler de novo.
     */
    public void prontoParaProcessar(Path path) throws InterruptedException {
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
