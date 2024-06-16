package com.example.testefrontiemb.service;

import com.example.testefrontiemb.models.RegistroContabil;
import org.hibernate.cfg.Environment;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.prefs.Preferences;
import org.apache.commons.io.FilenameUtils;
@Component
public class OrgArquivosService {


    public static void copiaArquivos(RegistroContabil registro) {
        Preferences prefs = Preferences.userRoot().node("Contabilidade-IEMB");

        String caminhoDestinoArquivoNota = prefs.get("pasta-destino", new JFileChooser().getFileSystemView().getDefaultDirectory().toString())
                + "\\" + registro.getAnoPrestacao() + "\\" + registro.getSemestrePrestacao() + "º Semestre" + "\\"
               + registro.getTipo() + "s" + "\\" + registro.getOrigemOuDestinacao()+ "s" + "\\"
               + registro.getId() + "-" + registro.getTitulo() + " ("
                + registro.getData().getDayOfMonth() + "-"
                + registro.getData().getMonth().getValue() + "-"
                + registro.getData().getYear() + ") - Nota Fiscal"
                + "." + FilenameUtils.getExtension(registro.getPathScanNotaFiscal());

        String caminhoDestinoArquivoComprovantes = prefs.get("pasta-destino", new JFileChooser().getFileSystemView().getDefaultDirectory().toString())
                + "\\" + registro.getAnoPrestacao() + "\\" + registro.getSemestrePrestacao() + "º Semestre" + "\\"
                + registro.getTipo() + "s" + "\\" + registro.getOrigemOuDestinacao()+ "s" + "\\"
                + registro.getId() + "-" + registro.getTitulo() + " ("
                + registro.getData().getDayOfMonth() + "-"
                + registro.getData().getMonth().getValue() + "-"
                + registro.getData().getYear() + ") - Comprovantes"
                + "." + FilenameUtils.getExtension(registro.getPathComprovantes());

        String caminhoDestinoArquivoFotos = prefs.get("pasta-destino", new JFileChooser().getFileSystemView().getDefaultDirectory().toString())
                + "\\" + registro.getAnoPrestacao() + "\\" + registro.getSemestrePrestacao() + "º Semestre" + "\\"
                + registro.getTipo() + "s" + "\\" + registro.getOrigemOuDestinacao()+ "s" + "\\"
                + registro.getId() + "-" + registro.getTitulo() + " ("
                + registro.getData().getDayOfMonth() + "-"
                + registro.getData().getMonth().getValue() + "-"
                + registro.getData().getYear() + ") - Fotos"
                + "." + FilenameUtils.getExtension(registro.getPathFotos());

        System.out.println("Caminho para onde será copiado o arquivo: " + caminhoDestinoArquivoNota);
        try {
            Files.createDirectories(Path.of(caminhoDestinoArquivoComprovantes).getParent());
            Path novoPath = null;

            //Copiar nota e atualizar o objeto registro, se o caminho não for vazio
            if(!registro.getPathScanNotaFiscal().isEmpty()) {
                novoPath = Files.copy(Path.of(registro.getPathScanNotaFiscal()),Path.of(caminhoDestinoArquivoNota), StandardCopyOption.REPLACE_EXISTING);
                registro.setPathScanNotaFiscal(novoPath.toString());
            }

            //Copiar Comprovante e atualizar o objeto registro, se o caminho não for vazio
            if(!registro.getPathComprovantes().isEmpty()){
                novoPath = Files.copy(Path.of(registro.getPathComprovantes()),Path.of(caminhoDestinoArquivoComprovantes), StandardCopyOption.REPLACE_EXISTING);
                registro.setPathComprovantes(novoPath.toString());
            }

            //Copiar Fotos e atualizar o objeto registro, se o caminho não for vazio
            if(!registro.getPathFotos().isEmpty()) {
                novoPath = Files.copy(Path.of(registro.getPathFotos()),Path.of(caminhoDestinoArquivoFotos), StandardCopyOption.REPLACE_EXISTING);
                registro.setPathFotos(novoPath.toString());
            }

            System.out.println("Copiou");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Não foi possível copiar os arquivos selecionados.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    public static void deletaArquivos(RegistroContabil registro) throws IOException {
        Files.delete(Path.of(registro.getPathScanNotaFiscal()));
        Files.delete(Path.of(registro.getPathComprovantes()));
        Files.delete(Path.of(registro.getPathFotos()));
    }
}
