package br.com.marcelo.processador;

import br.com.marcelo.processador.jobs.ProcuraArquivos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ProcessadorApplication implements CommandLineRunner {

    @Autowired
    ProcuraArquivos procuraArquivos;
    public static void main(String[] args) {
        SpringApplication.run(ProcessadorApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        procuraArquivos.buscaDadosDisco();
    }
}
