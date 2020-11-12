package br.com.marcelo.processador.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ItemVendaModel extends ModelGenerico {
    private long idVenda;
    private double quantidade;
    private double preco;
}
