package br.com.marcelo.processador.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class VendaModel extends ModelGenerico{
    private long idVenda;
    private List<ItemVendaModel> items;
    private String nomeVendedor;
}
