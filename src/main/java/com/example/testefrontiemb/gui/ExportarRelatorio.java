package com.example.testefrontiemb.gui;

import com.example.testefrontiemb.models.RegistroContabil;
import com.example.testefrontiemb.service.CsvService;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class ExportarRelatorio extends JFrame{
    private JButton buscarButton;
    @Setter
    private TelaPrincipal parent;
    private JPanel exportarRelatorioPanel;
    private JTextField nomeArquivoField;
    private JTextField destinoField;
    private JButton exportarButton;
    private JButton cancelarButton;
    private ArrayList<RegistroContabil> registros;

    public ExportarRelatorio(ArrayList<RegistroContabil> registros) {
        this.registros = registros;
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fileChooser.showOpenDialog(exportarRelatorioPanel);
                if(returnVal == JFileChooser.CANCEL_OPTION) {}
                destinoField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        exportarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    CsvService.exportarCsv(registros, destinoField.getText(),nomeArquivoField.getText());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(exportarRelatorioPanel,"Não foi possível exportar o arquivo como configurado. Erro: "
                            + ex.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
                }
                dispose();
            }
        });
    }
    public void exibir(Component parent) {
        this.setTitle("Exportar Relatório");
        this.setContentPane(exportarRelatorioPanel);
        this.pack();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }
}


