package com.example.testefrontiemb.gui;

import com.example.testefrontiemb.components.CustomDecimalFormatter;
import com.example.testefrontiemb.components.CustomTableCellRenderer;
import com.example.testefrontiemb.models.PeriodoPrestacao;
import com.example.testefrontiemb.models.RegistroContabil;
import com.example.testefrontiemb.service.CalculadoraService;
import com.example.testefrontiemb.service.OrgArquivosService;
import com.example.testefrontiemb.service.PeriodoService;
import com.example.testefrontiemb.service.RegistroService;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.prefs.Preferences;

@Component
public class TelaPrincipal extends JFrame{
    public static final String[] COLUNAS = new String[]{"ID", "TÍTULO", "DESCRIÇÃO", "TIPO", "DATA", "VALOR", "DESTINAÇÃO", "CPF/CNPJ", "Nº Nota Fiscal"};
    @Getter
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
    private JButton gerarRelatorioButton;
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
    private JButton definirPastaDeArmazenamentoButton;
    @Getter
    private JComboBox semestreComboBox;
    private RegistroService registroService;
   @Autowired
    private PeriodoService periodoService;
    ArrayList<RegistroContabil> registros;
    ArrayList<PeriodoPrestacao> periodos;
    public static final String FIRST_TIME_SETUP_PREF = "first-time-setup";
    public static final String PASTA_DESTINO_PREF = "pasta-destino";
    public static final String ULTIMO_ANO_PREST_VISUALIZADO = "ultimo-ano-pref";
    public static final String ULTIMO_SEMESTRE_PREST_VISUALIZADO = "ultimo-semestre-pref";
    Preferences prefs = Preferences.userRoot().node("Contabilidade-IEMB");
    CustomDecimalFormatter decimalFormatter;
    int anoRegistro;
    int semestreRegistro;
    TableRowSorter<DefaultTableModel> sorter;

    @Autowired
    public TelaPrincipal(RegistroService registroService, PeriodoService periodoService) {
        this.registroService = registroService;
        this.periodoService = periodoService;
        table.setFont(new Font("Serif", Font.BOLD, 16)); //Configura o tamanho da fonte
        table.setRowHeight(30);
        table.setDefaultRenderer(Object.class,new CustomTableCellRenderer());

        //Carrega os anos que estão no Banco de dados. Se não carregar nada, coloca 1967
        atualizaComboBox();
        if(anoComboBox.getSelectedItem() == null) {
            anoComboBox.addItem(1967);;//Para testes, remover em produção

        }
        semestreComboBox.addItem(1);
        semestreComboBox.addItem(2);

        anoComboBox.setSelectedItem(Integer.parseInt(prefs.get(ULTIMO_ANO_PREST_VISUALIZADO,"1967")));
        semestreComboBox.setSelectedItem(Integer.parseInt(prefs.get(ULTIMO_SEMESTRE_PREST_VISUALIZADO,"1")));

        atualizaTabela();
        atualizaDataPrestacao();

        exibir();
        //prefs.putBoolean(FIRST_TIME_SETUP_PREF,true); // Para fins de teste do first time setup. Remover em produção
        if(prefs.getBoolean(FIRST_TIME_SETUP_PREF,true)) firstTimeSetup();

        //Começa a inserir os listeners dos botões
        inserirDespesaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Verificar se há um ano setado
                InserirRegistro telaInserirDespesa = new InserirRegistro(registroService,
                        "Despesa", anoRegistro,semestreRegistro);
                telaInserirDespesa.setParent(TelaPrincipal.this);
                telaInserirDespesa.exibir(painel);
            }
        });
        novoAnoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    int novoAno = 0;
                    PeriodoPrestacao periodoNovo = null;
                    try {
                        novoAno = Integer.parseInt(JOptionPane.showInputDialog("Insira o novo ano para cadastrar:"));
                        periodoNovo = new PeriodoPrestacao(novoAno,1);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(TelaPrincipal.this,
                                "Insira um ano válido","Erro",JOptionPane.ERROR_MESSAGE);
                    }
                    periodoService.salvarPeriodo(periodoNovo);
                    periodoNovo = new PeriodoPrestacao(novoAno,2);
                    System.out.println("Vai tentar salvar o 2º período");
                    periodoService.salvarPeriodo(periodoNovo);
                    atualizaComboBox();
                    atualizaTabela();
            }
        });

        inserirReceitaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Verificar se há um ano setado
                InserirRegistro telaInserirDespesa = new InserirRegistro(registroService,"Receita", anoRegistro, semestreRegistro);
                telaInserirDespesa.setParent(TelaPrincipal.this);
                telaInserirDespesa.exibir(painel);
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    RegistroContabil registroEditar = registros.get(table.getSelectedRow());
                    System.out.println("É o registro de nome: " + registroEditar.getTitulo());
                    InserirRegistro telaEditarRegistro = new InserirRegistro(registroService,registroEditar);
                    telaEditarRegistro.setParent(TelaPrincipal.this);
                    telaEditarRegistro.exibir(painel);
                }
            }
        });
        definirPastaDeArmazenamentoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Mostra a pasta atual e confirma com o usuário se ele quer modificar ela
                int opcao = JOptionPane.showConfirmDialog(painel,"Sua pasta de armazenamento atual é:\n" +
                        prefs.get(PASTA_DESTINO_PREF,new JFileChooser().getFileSystemView().getDefaultDirectory().toString()) +
                        "\nVocê gostaria de mudar o local de armazenamento dos anexos?");

                //Se ele disser que quer modificar
                if (opcao == JOptionPane.YES_OPTION) {
                    //Seleciona a pasta nova
                    String pathPastaDestinoNova = selecionaPasta();
                    //Verifica se a pasta nova é uma pasta vazia
                    if(!pathPastaDestinoNova.isEmpty()) { //Se conseguiu pegar a pasta nova e ela está vazia

                        //Cria instância da pasta atual
                        final File PASTA_ATUAL = new File(prefs.get(PASTA_DESTINO_PREF,null)); //Instanciar a pasta atual para verificar se existem arquivos na pasta e, se existirem, copiar para a pasta nova

                        //Verifica se a pasta atual estava vazia
                        if(PASTA_ATUAL.listFiles().length == 0) { //Verificar se a pasta está vazia

                            //Se a atual está vazia, coloca a pasta nova nas configurações
                            prefs.put(PASTA_DESTINO_PREF,pathPastaDestinoNova);

                        } else { //Caso a pasta não esteja vazia
                            //Mensagem perguntando se quer copiar os arquivos
                            opcao = JOptionPane.showConfirmDialog(painel,"Parece que já existem objetos na pasta de destino atual. " +
                                    "\nVocê deseja mover eles para o novo destino?" +
                                    "\nVocê ainda terá de reassociar cada registro com a sua respectiva nota","Atenção",JOptionPane.YES_NO_CANCEL_OPTION);

                            //Caso o usuário queira copiar
                            if(opcao == JOptionPane.YES_OPTION) {
                                //Cria um objeto "File" da pasta nova
                                final File FILE_PASTA_DESTINO_NOVA = new File(pathPastaDestinoNova);

                                try {
                                    //Tenta copiar todos os conteúdos para a pasta nova
                                    FileUtils.copyDirectory(PASTA_ATUAL,FILE_PASTA_DESTINO_NOVA);
                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(painel,"Não foi possível copiar os conteúdos da pasta atual na pasta nova." +
                                            "\nPor segurança,nenhuma mudança foi feita nas duas configurações.","Erro",JOptionPane.ERROR_MESSAGE);
                                }

                                //Caso o usuário não queira copiar
                            } else if (opcao == JOptionPane.NO_OPTION) {
                                //Só coloca a pasta nova nas configurações
                                prefs.put(PASTA_DESTINO_PREF,pathPastaDestinoNova);
                            }
                        }
                    } else { //Se a pasta não estiver vazia
                        JOptionPane.showMessageDialog(painel, "A pasta selecionada não está vazia. " +
                                "\nPor favor selecione uma pasta vazia para continuar","Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }


                }
        });
        anoComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED){
                    System.out.println("Selecionou o ano " + anoComboBox.getSelectedItem() + ".");
                    semestreComboBox.setSelectedIndex(0);
                    prefs.put(ULTIMO_ANO_PREST_VISUALIZADO,String.valueOf(anoComboBox.getSelectedItem()));
                    prefs.put(ULTIMO_SEMESTRE_PREST_VISUALIZADO,String.valueOf(semestreComboBox.getSelectedItem()));
                    atualizaTabela();
                    atualizaDataPrestacao();
                }
            }
        });

        //Listener para se o usuário selecionar outro semestre
        semestreComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.out.println("Seleciou o " + semestreComboBox.getSelectedItem() + "semestre.");
                    atualizaTabela();
                    prefs.put(ULTIMO_SEMESTRE_PREST_VISUALIZADO,String.valueOf(semestreComboBox.getSelectedItem()));
                    atualizaDataPrestacao();
                }
            }
        });

        //Listener para se o usuário apertar delete selecionando a tabela
        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_DELETE) {
                    RegistroContabil registroDeletar = registros.get(table.getSelectedRow());
                    int opcao = JOptionPane.showConfirmDialog(TelaPrincipal.this,
                            "Você tem certeza que seja deletar o registro de título: " +
                            registroDeletar.getTitulo() + "?\n" +
                                    "Esta ação é PERMANENTE e não pode ser desfeita!","Deletar Registro",JOptionPane.ERROR_MESSAGE);
                    if(opcao == JOptionPane.YES_OPTION) {
                        registroService.deletarRegistro(registroDeletar);
                        try {
                            OrgArquivosService.deletaArquivos(registroDeletar);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(painel,"Não foi possível deletaar os arquivos associados. " +
                                    "\nÉ necessário que você apague os arquivos manualmente. A localização deles é:" +
                                    "\nNota fiscal: " + registroDeletar.getPathScanNotaFiscal() +
                                    "\nComprovante: "+ registroDeletar.getPathComprovantes() +
                                    "\nFotos: " + registroDeletar.getPathFotos(),
                                    "Erro ao deletar aquivos",JOptionPane.ERROR_MESSAGE);
                        }
                        atualizaTabela();
                    }
                }
            }
        });

        // Adiciona um DocumentListener ao JTextField para monitorar mudanças no texto
        pesquisaField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }

            private void filterTable() {
                String searchText = pesquisaField.getText();
                if (searchText.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
                }
            }
        });
        //Adicional listener do botão de gerar relatório
        gerarRelatorioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExportarRelatorio telaExportarRelatorio = new ExportarRelatorio(registros);
                telaExportarRelatorio.setParent(TelaPrincipal.this);
                telaExportarRelatorio.exibir(painel);
            }
        });
    }

    private void firstTimeSetup() {
        JOptionPane.showMessageDialog(painel,
                "Bem vindo! Como é a sua primeira vez utilizando o programa, por favor selecione a pasta onde ficarão salvos\n " +
                        "todos os anexos (PDFs das notas fiscais, fotografias etc). É recomendado que seja escolhida uma pasta \n" +
                        "do OneDrive, Google Drive ou outro serviço que faça backup em nuvem, por segurança",
                "Aviso",JOptionPane.INFORMATION_MESSAGE);
        do {
            String pathPastaDestino = selecionaPasta();
            if(!pathPastaDestino.equals("")) { //Se a pasta foi selecionada corretamente e está vazia
            System.out.println("Salvar caminho como padrão: " + pathPastaDestino);
            prefs.put(PASTA_DESTINO_PREF, pathPastaDestino);
            } else {
                JOptionPane.showMessageDialog(painel,"É necessário escolher uma pasta para salvar os anexos","Erro",JOptionPane.ERROR_MESSAGE);
            }
        } while (prefs.get(PASTA_DESTINO_PREF, "").equals(""));
        prefs.putBoolean(FIRST_TIME_SETUP_PREF,false);

    }

    private String selecionaPasta() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fileChooser.showOpenDialog(painel);
        System.out.println("Valor de Return Val: " + returnVal);
        if(returnVal == JFileChooser.CANCEL_OPTION) {
            return "";
        }
        String pastaDestino = fileChooser.getSelectedFile().getAbsolutePath();
        if(returnVal == JFileChooser.APPROVE_OPTION & Path.of(pastaDestino).toFile().exists()) {
            final File PASTA_ATUAL = new File(pastaDestino); //Instanciar a pasta atual para verificar se existem arquivos na pasta e, se existirem, copiar para a pasta nova
            if(PASTA_ATUAL.listFiles().length == 0) {
                return pastaDestino;

            } else {
                JOptionPane.showMessageDialog(painel,"A pasta selecionada deve estar vazia. Por favor selecione outra pasta","Erro",JOptionPane.ERROR_MESSAGE);
                return "";
            }
        } else {
            return "";
        }
    }

    public void atualizaTabela() {
        //Pegar o ano que deve ser utilizado para buscas
        anoRegistro = (int) anoComboBox.getSelectedItem();
        semestreRegistro = (int) semestreComboBox.getSelectedItem();

        // Puxar registros para povoar a tabela, conforme ano e semestre definidos pelo usuário
        registros = registroService.buscaPorAnoESemestre(anoRegistro,semestreRegistro);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        //Povoar a tabela com os dados obtidos do Banco de Dados
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
                    registro.getData().format(formatter),
                    decimalFormatter.converteParaVirgula(registro.getValor()),
                    registro.getOrigemOuDestinacao(),
                    registro.getCpfCnpj(),
                    registro.getNumNotaFiscal()
            });
        }

        // Criação do TableRowSorter e configuração na tabela
        sorter = new TableRowSorter<>(modelo);
        table.setRowSorter(sorter);

        table.setModel(modelo);
        atualizaCalculos(registros);
    }

    private void atualizaComboBox() {
        //Zerar a combobox
        anoComboBox.removeAllItems();

        //Atualizar os períodos de ano/semestre disponíveis
        periodos = periodoService.pegarUmPorSemestre();
        for(PeriodoPrestacao periodo : periodos){
            anoComboBox.addItem(periodo.getAno());
        }
    }

    private void atualizaCalculos(ArrayList<RegistroContabil> registros) {
        double limiteCusteio = CalculadoraService.calculaLimiteCusteio(registros);
        double limiteInvestimento = CalculadoraService.calculaLimiteInvestimento(registros);
        valorCustField.setText("R$ " + decimalFormatter.converteParaVirgula(limiteCusteio));
        valorInvestField.setText("R$ " + decimalFormatter.converteParaVirgula(limiteInvestimento));

        double valorTotalPrestacao = CalculadoraService.calculaValorPrestacaoContas(registros);
        valorPrestField.setText("R$ " + decimalFormatter.converteParaVirgula(valorTotalPrestacao));
    }

    private void atualizaDataPrestacao() {
        int ano = 0;
        int semestre = 0;
        try {
            ano = (Integer) anoComboBox.getSelectedItem();
            semestre = (Integer) semestreComboBox.getSelectedItem();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(painel,"Erro ao atualizar as datas. Por favor contate o administrador so sistema." +
                    "\n Erro: " + e.getMessage(),"Erro", JOptionPane.ERROR_MESSAGE);
        }
        if(semestre == 1) {
            dataProxPrestacaoField.setText("30/09/" + ano);
        } else if(semestre == 2) {
            dataProxPrestacaoField.setText("31/03/" + (ano + 1));
        }
    }

    public void exibir() {
        this.setTitle("Tela Principal");
        this.setContentPane(painel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 720);
        this.setLocationRelativeTo(null);
        table.getTableHeader().setReorderingAllowed(false);
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
        //Integer[] anosPlaceholder = {2020,2021,2022,2023,2024};
        //anoComboBox = new JComboBox<>(anosPlaceholder);//Combo Box dos anos

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
        /*String csvFile = "mockdata.csv"; // Path to your CSV file
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
        }*/
    }
}
