package br.com.marcelo.processador.dao;

import br.com.marcelo.processador.exception.ProcessadorException;
import br.com.marcelo.processador.model.ItemVendaModel;
import br.com.marcelo.processador.model.VendaModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VendaDAO extends DAOGenerico<VendaModel> {

    @Override
    public VendaModel getModel(String linha) throws ProcessadorException {
        String[] campos = super.separaLinha(linha);
        if (campos.length != 3) throw new ProcessadorException("Registro com número errado de campos", linha);
        long idVenda = converteLong(campos[0]);
        return new VendaModel(idVenda, getItensVenda(campos[1],idVenda), campos[2]);
    }

    private List<ItemVendaModel> getItensVenda(String vendas,long idVenda) throws ProcessadorException {
        String[] itens = super.separaArray(vendas);
        List<ItemVendaModel> result = new ArrayList<>();
        int i = 0;
        while (i<itens.length){
            if (i + 3 > itens.length) throw new ProcessadorException("Item venda incorreto", vendas);
            // converteLong(itens[i])
            result.add(new ItemVendaModel(idVenda,
                    converteDouble(itens[i + 1]),
                    converteDouble(itens[i + 2])));
            i = i + 3;
        }
        return result;
    }
}
