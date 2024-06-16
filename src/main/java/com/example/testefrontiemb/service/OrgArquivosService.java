package com.example.testefrontiemb.service;

import com.example.testefrontiemb.models.RegistroContabil;
import org.hibernate.cfg.Environment;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.prefs.Preferences;
import org.apache.commons.io.FilenameUtils;
@Component
public class OrgArquivosService {


    public static void copiaArquivos(RegistroContabil registro) {
        Preferences prefs = Preferences.userRoot().node("Contabilidade-IEMB");
        String caminhoDestinoArquivo = prefs.get("pasta-destino", new JFileChooser().getFileSystemView().getDefaultDirectory().toString())
                + "\\" + registro.getData().getYear() + "\\"
               + registro.getTipo() + "s" + "\\" + registro.getOrigemOuDestinacao()+ "s" + "\\"
               + registro.getId() + "-" + registro.getTitulo() + " ("
                + registro.getData().getDayOfMonth() + "-"
                + registro.getData().getMonth().getValue() + "-"
                + registro.getData().getYear() + ")"
                + "." + FilenameUtils.getExtension(registro.getPathScanNotaFiscal());
        System.out.println("Caminho para onde será copiado o arquivo: " + caminhoDestinoArquivo);
        try {
            Files.createDirectories(Path.of(caminhoDestinoArquivo).getParent());
            Path novoPath = Files.copy(Path.of(registro.getPathScanNotaFiscal()),Path.of(caminhoDestinoArquivo));
            registro.setPathScanNotaFiscal(novoPath.toString());
            System.out.println("Copiou");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
