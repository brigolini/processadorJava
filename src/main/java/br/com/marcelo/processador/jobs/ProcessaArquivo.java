package br.com.marcelo.processador.jobs;

import br.com.marcelo.processador.dao.ClienteDAO;
import br.com.marcelo.processador.dao.DAOGenerico;
import br.com.marcelo.processador.dao.VendaDAO;
import br.com.marcelo.processador.dao.VendedorDAO;
import br.com.marcelo.processador.enums.TipoRegistroEnum;
import br.com.marcelo.processador.exception.ProcessadorException;
import br.com.marcelo.processador.model.ClienteModel;
import br.com.marcelo.processador.model.ItemVendaModel;
import br.com.marcelo.processador.model.VendaModel;
import br.com.marcelo.processador.model.VendedorModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProcessaArquivo {

    @Autowired
    VendedorDAO vendedorDAO;

    @Autowired
    VendaDAO vendaDAO;

    @Autowired
    ClienteDAO clienteDAO;

    @Value("${br.com.marcelo.processador.entrada}")
    String entrada;

    @Value("${br.com.marcelo.processador.saida}")
    String saida;


    @Async("processaArquivoTaskExec")
    public void processaArquivo(List<String> arquivo, Path nomeArquivo) {
        List<ClienteModel> clientes = new ArrayList<>();
        List<VendaModel> vendas = new ArrayList<>();
        List<VendedorModel> vendedores = new ArrayList<>();
        arquivo.forEach(linha -> {
            try {
                TipoRegistroEnum reg = DAOGenerico.getTipoRegistro(linha);
                switch (reg) {
                    case vendedor:
                        vendedores.add(vendedorDAO.getModel(linha));
                        break;
                    case venda:
                        vendas.add(vendaDAO.getModel(linha));
                        break;
                    case cliente:
                        clientes.add(clienteDAO.getModel(linha));
                        break;
                }
            } catch (ProcessadorException e) {
                log.error(e.getMessage());
            }
        });
        try {

            Path saida = Paths.get(this.saida, nomeArquivo.toString());
            var resultadoPrograma = String.format("Quantidade de clientes no arquivo de entrada %d\n", clientes.size()) +
                    String.format("Quantidade de vendedores no arquivo de entrada %d\n", vendedores.size()) +
                    String.format("Id da Venda Mais cara %d\n", getIdVendaMaisCara(vendas)) +
                    String.format("O pior Vendedor %s\n", getPiorVendedor(vendas));
            Files.writeString(saida,resultadoPrograma);
        } catch (IOException e) {
            log.error("Não foi possível gravar no arquivo {}", saida);
        }

    }

    private long getIdVendaMaisCara(List<VendaModel> vendas) {
        var vendasAgrupadas = vendas
                .stream().map(VendaModel::getItems)
                .flatMap(List::stream).collect(Collectors.toList()).stream()
                .collect(Collectors.groupingBy(ItemVendaModel::getIdVenda,
                        Collectors.summingDouble(value -> value.getPreco() * value.getQuantidade())));
        long idMaiorVenda = 0;
        double maiorVenda = 0;
        for (Map.Entry<Long, Double> item : vendasAgrupadas.entrySet()) {
            if (maiorVenda < item.getValue()) {
                idMaiorVenda = item.getKey();
                maiorVenda = item.getValue();
            }
        }
        return idMaiorVenda;
    }

    private String getPiorVendedor(List<VendaModel> vendas) {
        var vendasAgrupadas = vendas
                .stream().collect(Collectors.groupingBy(VendaModel::getNomeVendedor));
        var piorVendedor = "";
        var piorTotalVenda = Double.MAX_VALUE;
        for (Map.Entry<String, List<VendaModel>> vendasVendedor : vendasAgrupadas.entrySet()) {
            double totalVendas = 0d;
            for (VendaModel vendaModel : vendasVendedor.getValue()) {
                totalVendas += (Double) vendaModel.getItems().stream().mapToDouble(value -> value.getQuantidade() * value.getPreco()).sum();
            }
            if (totalVendas < piorTotalVenda) {
                piorVendedor = vendasVendedor.getKey();
                piorTotalVenda = totalVendas;
            }
        }
        return piorVendedor;
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
