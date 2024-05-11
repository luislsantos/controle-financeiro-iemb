package com.example.testefrontiemb.gui;

import com.example.testefrontiemb.models.Despesa;
import com.example.testefrontiemb.service.RegistroService;
import lombok.Setter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class InserirDespesa extends JFrame{
    private JTextField tituloField;
    private JTextArea descriçãoField;
    private JSpinner dataField;
    private JTextField valorField;
    private JTextField cpfCnpjField;
    private JTextField numNotaFiscalField;
    private JTextField pathScanNotaField;
    private JButton salvarButton;
    private JButton cancelarButton;
    private JButton buscarButton;
    private JPanel painelInserirDesp;
    private JRadioButton custeioRadioButton;
    private JRadioButton investimentoRadioButton;
    private JTextField comprovanteTextField;
    private JButton buscarButton3;
    private JTextField fotosTextField;
    private JButton buscarButton1;
    private JLabel tituloLabel;
    private RegistroService registroService;
    @Setter
    private TelaPrincipal parent;


    public InserirDespesa(RegistroService registroService) {
        this.registroService = registroService;
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos PDF, JPEG ou PNG", "pdf", "jpg", "jpeg", "png");
                fileChooser.setFileFilter(filter);
                int returnVal = fileChooser.showOpenDialog(painelInserirDesp);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    pathScanNotaField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fechar a janela
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
                        (!custeioRadioButton.isSelected() & !investimentoRadioButton.isSelected()) |
                        cpfCnpjField.getText().isEmpty() |
                        numNotaFiscalField.getText().isEmpty() |
                        pathScanNotaField.getText().isEmpty()) {

                    mostraErroPreenchimento();

                }else{
                    String destinacao = null;
                    if(custeioRadioButton.isSelected()) destinacao = "Custeio";
                    if(investimentoRadioButton.isSelected()) destinacao = "Investimento";
                    Despesa despesa = new Despesa(
                            tituloField.getText(),
                            descriçãoField.getText(),
                            dataField.getValue().toString(),
                            Double.parseDouble(valorField.getText()),
                            destinacao,
                            cpfCnpjField.getText(),
                            numNotaFiscalField.getText(),
                            pathScanNotaField.getText(),
                            null);
                    registroService.salvarRegistro(despesa);
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
