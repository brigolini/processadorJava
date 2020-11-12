package br.com.marcelo.processador;


import br.com.marcelo.processador.service.ProcessadorService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
class ProcessadorTest {

    @Autowired
    ProcessadorService processadorService;

    @Test
    void processaDadosCorretamente() {
        List<String> linhas = new ArrayList<>();
        linhas.add("001ç1234567891234çPedroç50000");
        linhas.add("001ç3245678865434çPauloç40000.99");
        linhas.add("002ç2345675434544345çJose da SilvaçRural");
        linhas.add("002ç2345675433444345çEduardo PereiraçRural");
        linhas.add("003ç10ç[1-10-100-2-30-2.50-3-40-3.10]çPedro");
        linhas.add("003ç08ç[1-34-10-2-33-1.50-3-40-0.10]çPaulo");
        linhas.add("003ç08ç[1-34-10-2-33-1.50-3-40-0.10]çPaulo");

        processadorService.processaLinhas(linhas);
        assertThat(processadorService.clientes.size()).isEqualTo(2);
        assertThat(processadorService.vendedores.size()).isEqualTo(2);
        assertThat(processadorService.vendas.size()).isEqualTo(3);

    }

    @Test
    void processaVendasComErro() {
        List<String> linhas = new ArrayList<>();
        linhas.add("003ç10ç[1-103100-2-30-2.50-3-40-3.10]çPedro");
        linhas.add("003ç08ç[1-34-10-2-33-1.50-3-40-0.10]çPaulo");
        linhas.add("003ç08ç[1-34-10-2-33-1.50-3-40-0.10]çPaulo");
        processadorService.processaLinhas(linhas);

        assertThat(processadorService.vendas.size()).isEqualTo(2);
    }

    @Test
    void processaClientesComErro() {
        List<String> linhas = new ArrayList<>();
        linhas.add("002ç2345675434544345çJose da SilvaRural");
        linhas.add("002ç2345675433444345çEduardo PereiraçRural");
        processadorService.processaLinhas(linhas);

        assertThat(processadorService.clientes.size()).isEqualTo(1);
    }

    @Test
    void processaVendedoresComErro() {
        List<String> linhas = new ArrayList<>();
        linhas.add("001ç1234567891234çPedroç5000a0");
        linhas.add("001ç3245678865434ç40000.99");
        linhas.add("001ç3245678865434ç4çPauloç40000.99");
        linhas.add("001ç3245678865434çPauloç40000.99");
        processadorService.processaLinhas(linhas);

        assertThat(processadorService.vendedores.size()).isEqualTo(1);
    }

}
