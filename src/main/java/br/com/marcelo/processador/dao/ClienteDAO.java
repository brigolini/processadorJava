package br.com.marcelo.processador.dao;

import br.com.marcelo.processador.exception.ProcessadorException;
import br.com.marcelo.processador.model.ClienteModel;
import org.springframework.stereotype.Service;

@Service
public class ClienteDAO extends DAOGenerico<ClienteModel> {
    @Override
    public ClienteModel getModel(String linha) throws ProcessadorException {
        String[] campos = super.separaLinha(linha);
        if (campos.length!=3) throw new ProcessadorException("Registro com número errado de campos",linha);
        return new ClienteModel(converteLong(campos[0]),campos[1],campos[2]);
    }
}
