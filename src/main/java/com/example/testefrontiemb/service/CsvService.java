package com.example.testefrontiemb.service;

import com.example.testefrontiemb.models.RegistroContabil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;


public class CsvService {


    public static void exportarCsv(ArrayList<RegistroContabil> registros, String caminho, String nome) throws IOException {
        String caminhoCompleto = caminho + "\\" + nome + ".csv";
        ArrayList<String> campos = new ArrayList<>();
        for(Field campo: registros.get(0).getClass().getDeclaredFields()) {
            campos.add(campo.getName());
            System.out.println("Nome no campo: " + campo.getName());
        }
        String[] cabecalhos =new String[campos.size()];
        cabecalhos = campos.toArray(cabecalhos);
        System.out.println("Cabeçalhos do csv: " + Arrays.toString(cabecalhos));

        FileWriter writter = new FileWriter(caminhoCompleto);
        try (CSVPrinter printer = new CSVPrinter(writter, CSVFormat.EXCEL)) {
            printer.printRecord(cabecalhos);
            for(RegistroContabil registro : registros) {
                System.out.println("Registro guardado: " + registro.toStringCsv());
//                printer.printRecord(registro.toStringCsv());
                printer.printRecord(registro.getId(),
                        registro.getTipo(),
                        registro.getDescricao(),
                        registro.getTipo(),
                        registro.getData(),
                        registro.getValor(),
                        registro.getOrigemOuDestinacao(),
                        registro.getCpfCnpj(),
                        registro.getNumNotaFiscal(),
                        registro.getPathScanNotaFiscal(),
                        registro.getAnoPrestacao(),
                        registro.getSemestrePrestacao());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

//        CSVFormat csvFormat = CSVFormat.EXCEL.builder()
//                .setHeader(cabecalhos)
//                .build();
//
//        try {
//            CSVPrinter csvPrinter = new CSVPrinter(writter,csvFormat);
//
//            for(RegistroContabil registro : registros) {
//                System.out.println("Registro guardado: " + registro.toStringCsv());
//                //csvPrinter.printRecord(registro.toStringCsv());
//                csvPrinter.printRecord(registro.getId(),
//                        registro.getTipo(),
//                        registro.getDescricao(),
//                        registro.getTipo(),
//                        registro.getData(),
//                        registro.getValor(),
//                        registro.getOrigemOuDestinacao(),
//                        registro.getCpfCnpj(),
//                        registro.getNumNotaFiscal(),
//                        registro.getPathScanNotaFiscal(),
//                        registro.getAnoPrestacao(),
//                        registro.getSemestrePrestacao());
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("Aquivo CSV criado no caminho: " + caminhoCompleto);
    }

}
