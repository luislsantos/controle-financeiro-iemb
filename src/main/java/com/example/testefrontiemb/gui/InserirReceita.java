package com.example.testefrontiemb.gui;

import com.example.testefrontiemb.models.Receita;
import com.example.testefrontiemb.service.RegistroService;
import lombok.Setter;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

public class InserirReceita extends JFrame{
    private JPanel painelInserirDesp;
    private JLabel tituloLabel;
    private JTextField tituloField;
    private JTextArea descriçãoField;
    private JFormattedTextField dataField;
    private JTextField valorField;
    private JTextField cpfCnpjField;
    private JTextField numNotaFiscalField;
    private JTextField pathScanNotaField;
    private JButton salvarButton;
    private JButton cancelarButton;
    private JButton buscarButton;
    private JRadioButton sorteioRadioButton;
    private JRadioButton rateioRadioButton;
    private JTextField comprovanteTextField;
    private JButton buscarButton3;
    private JTextField fotosTextField;
    private JButton buscarButton1;
    private RegistroService registroService;
    @Setter
    TelaPrincipal parent;

    public InserirReceita(RegistroService registroService) {
        this.registroService = registroService;
        dataField.setFormatterFactory(new DefaultFormatterFactory(new DateFormatter(new SimpleDateFormat("dd/MM/yyyy"))));
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tituloField.getText().isEmpty() |
                        descriçãoField.getText().isEmpty() |
                        dataField.getValue().toString().isEmpty() |
                        valorField.getText().isEmpty() |
                        (!sorteioRadioButton.isSelected() & !rateioRadioButton.isSelected()) |
                        cpfCnpjField.getText().isEmpty() |
                        numNotaFiscalField.getText().isEmpty()/* |
                        pathScanNotaField.getText().isEmpty()*/) {

                    mostraErroPreenchimento();

                }else{
                    String origem = null;
                    if(sorteioRadioButton.isSelected()) origem = "Sorteio";
                    if(rateioRadioButton.isSelected()) origem = "Rateio";
                    Receita receita = new Receita(
                            tituloField.getText(),
                            descriçãoField.getText(),
                            dataField.getText(),
                            Double.parseDouble(valorField.getText()),
                            origem,
                            cpfCnpjField.getText(),
                            numNotaFiscalField.getText(),
                            pathScanNotaField.getText(),
                            null);
                    registroService.salvarRegistro(receita);
                    parent.atualizaTabela();
                    dispose();
                }
            }
        });
    }

    private void mostraErroPreenchimento() {
        JOptionPane.showMessageDialog(painelInserirDesp,"For favor preencha todos os campos","Erro",JOptionPane.ERROR_MESSAGE);
    }
    public void exibir(Component parent) {
        this.setTitle("Inserir Despesa");
        this.setContentPane(painelInserirDesp);
        this.pack();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }
}
