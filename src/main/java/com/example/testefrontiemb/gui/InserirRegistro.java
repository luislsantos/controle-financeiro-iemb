package com.example.testefrontiemb.gui;

import com.example.testefrontiemb.components.CustomDateTimeFormatter;
import com.example.testefrontiemb.models.RegistroContabil;
import com.example.testefrontiemb.service.RegistroService;
import lombok.Setter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class InserirRegistro extends JFrame{
    private JPanel painelEditarRegistro;
    private JLabel tituloLabel;
    private JTextField tituloField;
    private JTextArea descricaoField;
    private JTextField valorField;
    private JTextField cpfCnpjField;
    private JTextField numNotaFiscalField;
    private JTextField pathScanNotaField;
    private JButton salvarButton;
    private JButton cancelarButton;
    private JButton buscarButton;
    private JRadioButton custeioRadioButton;
    private JRadioButton investimentoRadioButton;
    private JTextField comprovanteTextField;
    private JButton buscarComprovantesButton;
    private JTextField fotosTextField;
    private JButton buscarFotosButton;
    private JFormattedTextField dataField;
    private JButton abrirButton;
    private JLabel cabecalhoLabel;
    private JLabel destinacaoField;
    private RegistroService registroService;
    @Setter
    private TelaPrincipal parent;
    CustomDateTimeFormatter formatter = new CustomDateTimeFormatter();

    /**
     * Construtor para quando se pretende inserir um registro novo
     * @param registroService Serviço para salvar no banco de dados
     * @param tipo Tipo de registro que será salvo. Pode ser Receita ou Despesa
     */
    public InserirRegistro(RegistroService registroService, String tipo) {
        this.registroService = registroService;
        dataField.setFormatterFactory(new DefaultFormatterFactory(new DateFormatter(new SimpleDateFormat("dd/MM/yyyy"))));

        //Definir Cabeçalho
        defineCabecalho(tipo);

        //Definir radio buttons. O padrão é ser despesa, então se for receita, vai editar
        defineRadioButtons(tipo);

        //Adiciona o event listener do botão buscar
        configuraBuscarbutton();
        //Adiciona o event listener do botão abrir
        configuraAbrirButton();
        //Adiciona o event listener do botão cancelar
        configuraCancelarButton();
        //Adiciona o event listener do botão salvar
        configuraSalvarButton(tipo, formatter.dateFormatter);
    }

    /**
     Construtor quando se pretende editar um reigstro
    * */
    public InserirRegistro(RegistroService registroService, RegistroContabil registroEditando) {
        this.registroService = registroService;
        dataField.setFormatterFactory(new DefaultFormatterFactory(new DateFormatter(new SimpleDateFormat("dd/MM/yyyy"))));

        //Definir Cabecalho
        cabecalhoLabel.setText("Editar Registro");

        //Definir radio buttons. O padrão é ser despesa, então se for receita, vai editar
        defineRadioButtons(registroEditando);

        //Preencher os campos se o registro ja tiver um ID
        defineCamposPreenchidos(registroEditando, formatter.dateFormatter);

        //Adiciona o event listener do botão buscar
        configuraBuscarbutton();
        //Adiciona o event listener do botão abrir
        configuraAbrirButton();
        //Adiciona o event listener do botão cancelar
        configuraCancelarButton();
        //Adiciona o event listener do botão salvar
        configuraSalvarButton(registroEditando, formatter.dateFormatter);
    }



    private void configuraCancelarButton() {
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fechar a janela
                dispose();
            }
        });
    }

    private void configuraAbrirButton() {
        abrirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().open(new File(pathScanNotaField.getText()));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(painelEditarRegistro,"Não foi possível abrir o arquivo informado. Talvez ele tenha sido movido","Erro",JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(painelEditarRegistro,"É necessário informar o caminho do arquivo","Erro",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void configuraBuscarbutton() {
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
    }

    /**
     * Configura o botão de salvar quando se estiver criando um registro novo
     * @param tipo Tipo do registro que será salvo
     * @param formatter Formatador de LocalDate
     */
    private void configuraSalvarButton(String tipo, DateTimeFormatter formatter) {
        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Verificar se algum dos campos está em branco
                if(tituloField.getText().isEmpty() |
                        descricaoField.getText().isEmpty() |
                        dataField.getText().isEmpty() |
                        valorField.getText().isEmpty() |
                        (!custeioRadioButton.isSelected() & !investimentoRadioButton.isSelected()) |
                        cpfCnpjField.getText().isEmpty() |
                        numNotaFiscalField.getText().isEmpty() |
                        pathScanNotaField.getText().isEmpty()) {

                    mostraErroPreenchimento();

                }else{
                    String origem = null;
                    if(tipo.equals("Receita")) {
                        if(custeioRadioButton.isSelected()) origem = "Sorteio";
                        if(investimentoRadioButton.isSelected()) origem = "Rateio";
                    } else if(tipo.equals("Despesa")) {
                        if(custeioRadioButton.isSelected()) origem = "Custeio";
                        if(investimentoRadioButton.isSelected()) origem = "Investimento";
                    }
                    RegistroContabil registro = new RegistroContabil(
                            tituloField.getText(),
                            descricaoField.getText(),
                            tipo,
                            LocalDate.parse(dataField.getText(),formatter),
                            Double.parseDouble(valorField.getText()),
                            origem,
                            cpfCnpjField.getText(),
                            numNotaFiscalField.getText(),
                            pathScanNotaField.getText());
                    //orgArquivosService.copiaArquivos(receita);
                    registroService.salvarRegistro(registro);
                    parent.atualizaTabela();
                    dispose();
                }
            }
        });
    }

    /**
     * Configura o botão de salvar quando se estiver editando um registro já criado
     * @param registroEditando Registro que está sendo editado
     * @param formatter
     */
    private void configuraSalvarButton(RegistroContabil registroEditando, DateTimeFormatter formatter) {
        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Verificar se algum dos campos está em branco
                if(tituloField.getText().isEmpty() |
                        descricaoField.getText().isEmpty() |
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
                    registroEditando.setDescricao(descricaoField.getText());
                    registroEditando.setData(LocalDate.parse(dataField.getText(), formatter));
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
    }

    /**
     * Define os campos preenchidos, quando se estiver editando um registro
     * @param registroEditando Registro que está sendo editado
     * @param formatter Formatador para LocalDate
     */
    private void defineCamposPreenchidos(RegistroContabil registroEditando, DateTimeFormatter formatter) {
        this.tituloField.setText(registroEditando.getTitulo());
        this.descricaoField.setText(registroEditando.getDescricao());
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
    }

    /**
     * Método para definir a descrição e o tipo de radio buttons quando se está criando um registro novo
     * @param tipo Tipo de registro, pode ser Receita ou Despesa
     */
    private void defineRadioButtons(String tipo) {
        if (tipo.equals("Receita")) {
            destinacaoField.setText("Origem");
            custeioRadioButton.setText("Sorteio");
            investimentoRadioButton.setText("Rateio");
        }
    }

    /**
     * Método para definir a descrição eo tipo dos radio buttons, quando se está editando um registro
     * @param registroEditando Registro que está sendo editado
     */

    private void defineRadioButtons(RegistroContabil registroEditando) {
        if (registroEditando.getTipo().equals("Receita")) {
            destinacaoField.setText("Origem");
            custeioRadioButton.setText("Sorteio");
            investimentoRadioButton.setText("Rateio");
        }
    }

    private void defineCabecalho(String tipo) {

        if(tipo.equals("Receita")) {
            cabecalhoLabel.setText("Inserir Receita");
        }
        if(tipo.equals("Despesa")) {
            cabecalhoLabel.setText("Inserir Despesa");

        }
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
