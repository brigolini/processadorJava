package br.com.marcelo.processador.exception;

public class ProcessadorException extends Exception {
    public ProcessadorException(String erro,String item){
        super(String.format("Erro: %s - Dado: %s ",erro,item));
    }
}
