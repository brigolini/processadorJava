package br.com.marcelo.processador.util;

import br.com.marcelo.processador.exception.ProcessadorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DirHelper {

    @Value("${br.com.marcelo.processador.entrada}")
    String entrada;

    @Value("${br.com.marcelo.processador.saida}")
    String saida;

    public Path getDirEntrada() throws ProcessadorException {

        Path path = Paths.get(System.getProperty("user.home"), entrada);
        return criaSeNaoExiste(path);
    }

    public Path getDirSaida() throws ProcessadorException {
        Path path = Paths.get(System.getProperty("user.home"), saida);
        return criaSeNaoExiste(path);
    }

    public Path criaSeNaoExiste(Path path) throws ProcessadorException {
        File file = new File(path.toString());
        if (!(file.exists())) {
            if (file.mkdirs())
                return path;
            else
                throw new ProcessadorException("Não é possível criar o diretório", path.toString());
        }
        return path;
    }
}
