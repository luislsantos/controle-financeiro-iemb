package com.example.testefrontiemb.gui;

import com.example.testefrontiemb.models.RegistroContabil;
import com.example.testefrontiemb.service.CalculadoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.testefrontiemb.service.RegistroService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Currency;

@Component
public class TelaPrincipal extends JFrame{
    public static final String[] COLUNAS = new String[]{"ID", "TÍTULO", "DESCRIÇÃO", "TIPO", "DATA", "VALOR", "DESTINAÇÃO", "CPF/CNPJ", "Nº Nota Fiscal"};
    private JComboBox anoComboBox;
    private JButton novoAnoButton;
    private JTextField pesquisaField;
    private JFormattedTextField valorInvestField;
    private JTextField valorCustField;
    private JTextField dataProxPrestacaoField;
    private JTextField valorPrestField;
    private JTextField faltaGastarCustField;
    private JTextField faltaGastarInvestField;
    private JPanel cabecalho;
    private JButton gerarRelatórioButton;
    private JPanel painelLateral;
    private JPanel painel;
    private JPanel footer;
    private JScrollPane tabelaScroll;
    private JTable table;
    private JButton inserirDespesaButton;
    private JButton inserirReceitaButton;
    private JLabel valorInvestLabel;
    private JLabel valorCustLabel;
    private JLabel dataProxPrestLabel;
    private JLabel valorPrestLabel;
    private JLabel faltaGastarCustLabel;
    private JLabel faltaGastarInvestLabel;
    private RegistroService registroService;
    @Autowired
    public TelaPrincipal(RegistroService registroService) {
        this.registroService = registroService;
        atualizaTabela();

        exibir();
        inserirDespesaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InserirDespesa telaInserirDespesa = new InserirDespesa(registroService);
                telaInserirDespesa.setParent(TelaPrincipal.this);
                telaInserirDespesa.exibir(painel);
            }
        });
        novoAnoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizaTabela();
            }
        });

        inserirReceitaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InserirReceita telaInserirDespesa = new InserirReceita(registroService);
                telaInserirDespesa.setParent(TelaPrincipal.this);
                telaInserirDespesa.exibir(painel);
            }
        });
    }

    public void atualizaTabela() {
        registroService.lerRegistrosParaTeste();
        ArrayList<RegistroContabil> registros = registroService.lerTudo();
        DefaultTableModel modelo = new DefaultTableModel(COLUNAS,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (RegistroContabil registro:registros) {
            modelo.addRow(new String[]{
                    registro.getId().toString(),
                    registro.getTitulo(),
                    registro.getDescricao(),
                    registro.getTipo(),
                    registro.getData().toString(),
                    String.valueOf(registro.getValor()),
                    registro.getOrigemOuDestinacao(),
                    registro.getCpfCnpj(),
                    registro.getNumNotaFiscal()
            });
        }
        table.setModel(modelo);
        atualizaCalculos(registros);
    }

    private void atualizaCalculos(ArrayList<RegistroContabil> registros) {
        double limiteCusteio = CalculadoraService.calculaLimiteCusteio(registros);
        double limiteInvestimento = CalculadoraService.calculaLimiteInvestimento(registros);
        valorCustField.setText(String.valueOf(limiteCusteio));
        valorInvestField.setText(String.valueOf(limiteInvestimento));
    }

    /*public static void main(String[] args) {
        JFrame frame = new JFrame("TelaPrincipal");
        frame.setContentPane(new TelaPrincipal()).painel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }*/
    public void exibir() {
        this.setTitle("Tela Principal");
        this.setContentPane(painel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 720);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
    }

    private JFormattedTextField createCustomFormattedTextField() {
        // Create a decimal format with comma as decimal separator and "R$ " prefix
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');

        DecimalFormat decimalFormat = new DecimalFormat("R$ #,##0.00", symbols);
        decimalFormat.setParseBigDecimal(true); // Enable parsing as BigDecimal for accuracy

        // Create a number formatter with the custom decimal format
        NumberFormatter numberFormatter = new NumberFormatter(decimalFormat);
        numberFormatter.setValueClass(BigDecimal.class); // Set value class to BigDecimal

        // Create a formatter factory with the custom number formatter
        DefaultFormatterFactory formatterFactory = new DefaultFormatterFactory(numberFormatter);

        // Create and configure the formatted text field
        JFormattedTextField formattedTextField = new JFormattedTextField();
        formattedTextField.setFormatterFactory(formatterFactory);
        formattedTextField.setColumns(15); // Set preferred width of the field
        formattedTextField.setValue(BigDecimal.ZERO); // Set initial value

        return formattedTextField;
    }

    private void createUIComponents() {

        valorInvestField = createCustomFormattedTextField();
        valorCustField = createCustomFormattedTextField();
        // Mock data para a combobox de anos
        Integer[] anosPlaceholder = {2020,2021,2022,2023,2024};
        anoComboBox = new JComboBox<>(anosPlaceholder);//Combo Box dos anos

        // Mock data para a tabela
        table = new JTable();
        DefaultTableModel modelo = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) { //Fazer com que a tabela não seja editável
                return false;
            }
        };
        table.setModel(modelo);

        // Read CSV and populate table model
        String csvFile = "mockdata.csv"; // Path to your CSV file
        String line;
        String[] columnNames;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Read column names from the first line
            if ((line = br.readLine()) != null) {
                columnNames = line.split(",");
                for (String columnName : columnNames) {
                    modelo.addColumn(columnName);
                }
            }

            // Read data lines
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                modelo.addRow(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
