package br.com.marcelo.processador.service;

import br.com.marcelo.processador.dao.ClienteDAO;
import br.com.marcelo.processador.dao.DAOGenerico;
import br.com.marcelo.processador.dao.VendaDAO;
import br.com.marcelo.processador.dao.VendedorDAO;
import br.com.marcelo.processador.enums.TipoRegistroEnum;
import br.com.marcelo.processador.exception.ProcessadorException;
import br.com.marcelo.processador.model.ClienteModel;
import br.com.marcelo.processador.model.VendaModel;
import br.com.marcelo.processador.model.VendedorModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProcessadorService {

    @Autowired
    VendedorDAO vendedorDAO;

    @Autowired
    VendaDAO vendaDAO;

    @Autowired
    ClienteDAO clienteDAO;

    public List<ClienteModel> clientes;
    public List<VendaModel> vendas;
    public List<VendedorModel> vendedores;

    public void processaLinhas(List<String> arquivo) {
        clientes = new ArrayList<>();
        vendas = new ArrayList<>();
        vendedores = new ArrayList<>();

        arquivo.forEach(linha -> {
            TipoRegistroEnum reg = null;
            try {
                reg = DAOGenerico.getTipoRegistro(linha);
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
                log.error("Erro ao carregar linha {}. Mensagem: {}",linha,e.getMessage());
            }

        });
    }

}
