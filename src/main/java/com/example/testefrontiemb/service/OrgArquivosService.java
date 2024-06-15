package com.example.testefrontiemb.service;

import com.example.testefrontiemb.models.RegistroContabil;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.prefs.Preferences;
@Component
public class OrgArquivosService {


    public static void copiaArquivos(RegistroContabil registro) {
        Preferences prefs = Preferences.userRoot().node("Contabilidade-IEMB");
        String caminhoDestinoArquivo = "pasta-destino" + "\\" + registro.getTipo() + "\\" +
                registro.getData().getYear() + "\\" + registro.getOrigemOuDestinacao() + "\\" +
                registro.getId() + "-" + registro.getTitulo();
        try {
            Path novoPath = Files.copy(Path.of(registro.getPathScanNotaFiscal()),Path.of(caminhoDestinoArquivo));
            registro.setPathScanNotaFiscal(novoPath.toString());
            System.out.println("Copiou");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
