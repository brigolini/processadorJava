package br.com.marcelo.processador.dao;

import br.com.marcelo.processador.exception.ProcessadorException;
import br.com.marcelo.processador.model.ClienteModel;
import br.com.marcelo.processador.model.ModelGenerico;
import br.com.marcelo.processador.model.VendedorModel;
import org.springframework.stereotype.Service;

@Service
public class VendedorDAO extends DAOGenerico<VendedorModel> {
    @Override
    public VendedorModel getModel(String linha) throws ProcessadorException {
        String[] campos = super.separaLinha(linha);
        if (campos.length!=3) throw new ProcessadorException("Vendedor com número errado de campos",linha);
        return new VendedorModel(converteLong(campos[0]),campos[1],converteDouble(campos[2]));
    }
}
