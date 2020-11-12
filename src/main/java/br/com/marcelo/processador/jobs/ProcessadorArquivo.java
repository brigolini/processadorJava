package br.com.marcelo.processador.jobs;

import br.com.marcelo.processador.service.ProcessadorService;
import br.com.marcelo.processador.util.DirHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static br.com.marcelo.processador.service.AglutinadorService.getIdVendaMaisCara;
import static br.com.marcelo.processador.service.AglutinadorService.getPiorVendedor;

@Service
@Slf4j
public class ProcessadorArquivo {

    @Autowired
    DirHelper dirHelper;

    @Autowired
    ProcessadorService processadorService;


    @Async("processaArquivoTaskExec")
    public void processarArquivo(List<String> arquivo, Path nomeArquivo) {

            processadorService.processaLinhas(arquivo);
            salvaInformacoes(nomeArquivo,
                    getIdVendaMaisCara(processadorService.vendas),
                    getPiorVendedor(processadorService.vendas),
                    processadorService.clientes.size(),
                    processadorService.vendedores.size());
    }



    private void salvaInformacoes(Path nomeArquivo, double vendaMaisCara, String piorVendedor,
                                  long totalClientes, long totalVendedores) {
        try {
            Path saida = Paths.get(dirHelper.getDirSaida().toString(), nomeArquivo.toString());
            var resultadoPrograma = String.format("Quantidade de clientes no arquivo de entrada %d\n", totalClientes) +
                    String.format("Quantidade de vendedores no arquivo de entrada %d\n", totalVendedores) +
                    String.format("Id da Venda Mais cara %f\n", vendaMaisCara ) +
                    String.format("O pior Vendedor %s\n", piorVendedor);
            Files.writeString(saida,resultadoPrograma);
        } catch (IOException e) {
            log.error("Não foi possível gravar no arquivo {}", nomeArquivo);
        }
    }


    @Bean("processaArquivoTaskExec")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("processamento-arquivo-");
        return executor;
    }
}
