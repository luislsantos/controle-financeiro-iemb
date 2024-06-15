package com.example.testefrontiemb.gui;

import com.example.testefrontiemb.models.RegistroContabil;
import com.example.testefrontiemb.service.RegistroService;
import lombok.Setter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class EditarRegistro extends JFrame{
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
    private JPanel painelEditarRegistro;
    private JRadioButton custeioRadioButton;
    private JRadioButton investimentoRadioButton;
    private JTextField comprovanteTextField;
    private JButton buscarButton3;
    private JTextField fotosTextField;
    private JButton buscarButton1;
    private JLabel tituloLabel;
    private JButton abrirButton;
    private RegistroService registroService;
    @Setter
    private TelaPrincipal parent;


    public EditarRegistro(RegistroService registroService, RegistroContabil registroEditando) {
        this.registroService = registroService;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        //Ver se é receita ou despesa, e ajustar os campos adequadamente
        if (registroEditando.getTipo().equals("Receita")) {
            descriçãoField.setText("Origem");
            custeioRadioButton.setText("Sorteio");
            investimentoRadioButton.setText("Rateio");
        }

        //Preencher os campos
        this.tituloField.setText(registroEditando.getTitulo());
        this.descriçãoField.setText(registroEditando.getDescricao());
        this.dataField.setText(registroEditando.getData().format(formatter));
        this.valorField.setText(String.valueOf(registroEditando.getValor()));
        //Setar os radio buttons de acordo com o que tiver marcado
        if(registroEditando.getTipo().equals("Receita")) {
            if(registroEditando.getOrigemOuDestinacao().equals("Sorteio")) {custeioRadioButton.setSelected(true);}
            if(registroEditando.getOrigemOuDestinacao().equals("Rateio")) {investimentoRadioButton.setSelected(true);}
        } else if (registroEditando.getTipo().equals("Despesa")) {
            if(registroEditando.getOrigemOuDestinacao().equals("Custeio")) {custeioRadioButton.setSelected(true);}
            if(registroEditando.getOrigemOuDestinacao().equals("Investimento")) {investimentoRadioButton.setSelected(true);}

        }
        this.cpfCnpjField.setText(registroEditando.getCpfCnpj());
        this.numNotaFiscalField.setText(registroEditando.getNumNotaFiscal());
        this.pathScanNotaField.setText(registroEditando.getPathScanNotaFiscal());
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos PDF, JPEG ou PNG", "pdf", "jpg", "jpeg", "png");
                fileChooser.setFileFilter(filter);
                int returnVal = fileChooser.showOpenDialog(painelEditarRegistro);
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
                //Verificar se algum dos campos está em branco
                if(tituloField.getText().isEmpty() |
                        descriçãoField.getText().isEmpty() |
                        dataField.getText().isEmpty() |
                        valorField.getText().isEmpty() |
                        (!custeioRadioButton.isSelected() & !investimentoRadioButton.isSelected()) |
                        cpfCnpjField.getText().isEmpty() |
                        numNotaFiscalField.getText().isEmpty() |
                        pathScanNotaField.getText().isEmpty()) {

                    mostraErroPreenchimento();

                }else{
                    String destinacao = null;
                    //Verificar se o que está sendo editado é uma receita ou uma despesa
                    if (registroEditando.getTipo().equals("Receita")) {
                        if(custeioRadioButton.isSelected()) destinacao = "Soteio";
                        if(investimentoRadioButton.isSelected()) destinacao = "Rateio";
                    } else if (registroEditando.getTipo().equals("Despesa")) {
                        if(custeioRadioButton.isSelected()) destinacao = "Custeio";
                        if(investimentoRadioButton.isSelected()) destinacao = "Investimento";
                    }

                    //Substituir os campos do registro conforme modelo
                    registroEditando.setTitulo(tituloField.getText());
                    registroEditando.setDescricao(descriçãoField.getText());
                    registroEditando.setData(LocalDate.parse(dataField.getText(),formatter));
                    registroEditando.setValor(Double.parseDouble(valorField.getText()));
                    registroEditando.setOrigemOuDestinacao(destinacao);
                    registroEditando.setCpfCnpj(cpfCnpjField.getText());
                    registroEditando.setNumNotaFiscal(numNotaFiscalField.getText());
                    registroEditando.setPathScanNotaFiscal(pathScanNotaField.getText());
                    registroService.salvarRegistro(registroEditando);
                    parent.atualizaTabela();
                    dispose();
                }
            }
        });
        abrirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().open(new File(pathScanNotaField.getText()));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(painelEditarRegistro,"Não foi possível abrir o arquivo informado. Talvez ele tenha sido movido","Erro",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void mostraErroPreenchimento() {
        JOptionPane.showMessageDialog(painelEditarRegistro,"For favor preencha todos os campos","Erro",JOptionPane.ERROR_MESSAGE);
    }

    public void exibir(Component parent) {
        this.setTitle("Inserir Despesa");
        this.setContentPane(painelEditarRegistro);
        this.pack();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

}
