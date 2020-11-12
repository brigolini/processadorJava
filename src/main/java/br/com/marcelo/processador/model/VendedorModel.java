package br.com.marcelo.processador.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class VendedorModel extends ModelGenerico{
    private long cpf;
    private String nome;
    private Double salario;
}
