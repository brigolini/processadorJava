package br.com.marcelo.processador;

import br.com.marcelo.processador.service.AglutinadorService;
import br.com.marcelo.processador.service.ProcessadorService;
import org.assertj.core.api.Assertions;
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
public class CalculosTest {

    @Autowired
    ProcessadorService processadorService;

    @Test
    void processaDadosCorretamente() {
        List<String> linhas = new ArrayList<>();
        linhas.add("001ç1234567891234çPedroç50000");
        linhas.add("001ç3245678865434çPauloç40000.99");
        linhas.add("002ç2345675434544345çJose da SilvaçRural");
        linhas.add("002ç2345675433444345çEduardo PereiraçRural");
        linhas.add("003ç10ç[1-10-100-2-30-3.0-3-40-3]çPedro");
        linhas.add("003ç08ç[1-34-10-2-33-1-3-40-0.10]çPaulo");
        linhas.add("003ç04ç[1-34-10-2-33-2-3-40-0.10]çPaulo");

        processadorService.processaLinhas(linhas);
        Assertions.assertThat(AglutinadorService.getIdVendaMaisCara(processadorService.vendas)).isEqualTo(10);
        assertThat(AglutinadorService.getPiorVendedor(processadorService.vendas)).isEqualTo("Paulo");

    }


}
