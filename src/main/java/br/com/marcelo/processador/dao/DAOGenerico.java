package br.com.marcelo.processador.dao;

import br.com.marcelo.processador.enums.TipoRegistroEnum;
import br.com.marcelo.processador.exception.ProcessadorException;
import br.com.marcelo.processador.model.ModelGenerico;

import java.lang.reflect.Field;
import java.util.Arrays;

public abstract class DAOGenerico <T extends ModelGenerico>{
    private static final String separador = "ç";
    private static final String separadorArray = "-";

    protected String[] separaLinha(String linha){
        return Arrays.copyOfRange(linha.split(separador),1,linha.split(separador).length);
    }

    protected String[] separaArray(String arr){
        return arr.replaceAll("\\[","")
                .replaceAll("]","")
                .split(separadorArray);
    }

    public static TipoRegistroEnum getTipoRegistro(String linha) throws ProcessadorException {
        String id = linha.substring(0,linha.indexOf(separador));
        if (id.equals("001")) return TipoRegistroEnum.vendedor;
        if (id.equals("002")) return TipoRegistroEnum.cliente;
        if (id.equals("003")) return TipoRegistroEnum.venda;
        throw new ProcessadorException("ID de Registro Inexistente",linha);
    }

    public static double converteDouble(String valor) throws ProcessadorException {
        try{
            return Double.parseDouble(valor);
        } catch (Exception e){
            throw new ProcessadorException("Conversão para Double não permitida",valor);
        }
    }

    public static long converteLong(String valor) throws ProcessadorException {
        try{
            return Long.parseLong(valor);
        } catch (Exception e){
            throw new ProcessadorException("Conversão para Long não permitida",valor);
        }
    }

    public abstract T getModel (String linha) throws ProcessadorException;
}
