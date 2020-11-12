package br.com.marcelo.processador.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ClienteModel extends ModelGenerico {
    private long cnpj;
    private String nome;
    private String bussinessArea;
}
