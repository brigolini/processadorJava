package br.com.marcelo.processador.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DirHelper {

    @Value("${br.com.marcelo.processador.entrada}")
    String entrada;

    @Value("${br.com.marcelo.processador.saida}")
    String saida;

    public Path getDirEntrada(){
        return Paths.get(System.getProperty("user.home"),entrada);
    }

    public Path getDirSaida(){
        return Paths.get(System.getProperty("user.home"),saida);
    }
}
