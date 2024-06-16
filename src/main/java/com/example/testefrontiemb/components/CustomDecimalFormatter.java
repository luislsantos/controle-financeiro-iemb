package com.example.testefrontiemb.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;
@Component
public class CustomDecimalFormatter {
    static DecimalFormatSymbols symbols;
    static String pattern;
    static DecimalFormat decimalFormat;

    public CustomDecimalFormatter() {
        // Crie um objeto DecimalFormatSymbols e defina os símbolos desejados
        symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        // Crie o padrão desejado para a formatação
        pattern = "#,##0.00";

        // Crie o padrão desejado para a formatação
        decimalFormat = new DecimalFormat(pattern, symbols);
    }
    @Autowired
    public static String converteParaVirgula(double numero) {


        return decimalFormat.format(numero);

    }

    @Autowired
    public static double converteParaFloat(String numComVirgula) throws ParseException {
        Number numero = decimalFormat.parse(numComVirgula);
        return numero.doubleValue();
    }

}
