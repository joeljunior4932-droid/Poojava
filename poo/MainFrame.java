import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame { 
    private SistemaController controller;
    private MedicoesPanel medicoesPanel;
    private FiltrosPanel filtrosPanel;
    private RegressaoPanel regressaoPanel;
    private GraficoPanel graficoPanel;

    private boolean arquivoAberto = false;

    public MainFrame() {
        setTitle("Sistema de previsão de consumo energético");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        controller = new SistemaController(this);

        criarPaineis();
        criarMenuBar();
        conectarEventos();

        //trata o x da janela
        addWindowListener (new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                sair();
            }
        });
        setVisible (true);
    }

    private void criarPaineis() {
        medicoesPanel = new MedicoesPanel();
        filtrosPanel = new FiltrosPanel();
        regressaoPanel = new RegressaoPanel();
        graficoPanel = new GraficoPanel();

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Medicoes", medicoesPanel);
        abas.addTab("Filtros", filtrosPanel);
        abas.addTab("Regressão e Previsão", regressaoPanel);
        abas.addTab("Gráfico", graficoPanel);
        add(abas, BorderLayout.CENTER);
    }

    private void criarMenuBar() {
        JMenuBar menuBar = new JMenuBar(); 
        JMenu menuArquivo = new JMenu("Arquivo");

        JMenuItem itemCarregar = new JMenuItem("Carregar TSV");
        itemCarregar.addActionListener( e -> {
            controller.carregarArquivo();
            arquivoAberto = !controller.getTabelaModel().getMedicoes().isEmpty();
        });

        JMenuItem itemExportar = new JMenuItem("Exportar relatório TSV");
        itemExportar.addActionListener(e-> exportar());

        JMenuItem itemFechar = new JMenuItem("Fechar arquivo");
        itemFechar.addActionListener(e-> fecharArquivo());

        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e-> sair());

        menuArquivo.add(itemCarregar);
        menuArquivo.add(itemExportar);
        menuArquivo.add(itemFechar);
        menuArquivo.addSeparator();
        menuArquivo.add(itemSair);

        //menu filtros 
        JMenu menuFiltros = new JMenu("Filtros");
        JMenuItem itemLimparFiltros = new JMenuItem("Limpar todos os filtros");
        itemLimparFiltros.addActionListener(e-> controller.limparFiltros());

        menuFiltros.add(itemLimparFiltros);
        menuBar.add(menuArquivo);
        menuBar.add(menuFiltros);

        setJMenuBar(menuBar);
    }
    private void conectarEventos(){
        //conectar msm tabelaModel
        medicoesPanel.setTabelaModel(controller.getTabelaModel());

        //aplicar o renderer em tds as colunas da tab 
        OutlierTableCellRenderer renderer = controller.getRenderer();
        for (int i = 0; i < medicoesPanel.getTabela().getColumnCount(); i++){
            medicoesPanel.getTabela().getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        //botoes aba medicoes
        medicoesPanel.getBtnAdicionar().addActionListener(e-> {
            controller.adicionarMedicao();
        });

        medicoesPanel.getBtnRemover().addActionListener(e-> {
            controller.removerSelecionadas();
        });

        //filtros
        filtrosPanel.getBtnAplicar().addActionListener(e-> {
            controller.aplicarFiltros();
        });

        filtrosPanel.getBtnLimpar().addActionListener(e-> {
            controller.limparFiltros();
        });

        //slider outlier - muda o limite ao arrastar
        regressaoPanel.getSliderOutlier().addChangeListener(e-> {
            int limite = regressaoPanel.getSliderOutlier().getValue();
            boolean excluir = regressaoPanel.getBtnExcluirOutliers().isSelected();
            controller.aplicarExclusaoOutliers(limite, excluir);
        });

        //excluir/destacar - reaplica c o novo estado
            regressaoPanel.getBtnExcluirOutliers().addActionListener(e-> {
            int limite = regressaoPanel.getSliderOutlier().getValue();
            boolean excluir = regressaoPanel.getBtnExcluirOutliers().isSelected();
            controller.aplicarExclusaoOutliers(limite, excluir);
        });
    }

    private void exportar(){
        if (!arquivoAberto) {
            JOptionPane.showMessageDialog(this, "Nenhum arquivo carregado para exportar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        controller.exportarTSV();
    }

    //fechar arq 
    private void fecharArquivo() {
        if (!arquivoAberto) 
            return;

        int opcao = JOptionPane.showConfirmDialog(this, "Deseja exportar o relatório antes de fechar?", "Fechar arquivo", JOptionPane.YES_NO_CANCEL_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
            exportar();
        }
        if (opcao == JOptionPane.CANCEL_OPTION) {
            return;
        }

        //limpa td 
        controller.limparTabela();
        controller.limparFiltros();
        arquivoAberto = false;
    }

    private void sair(){
        if (arquivoAberto) {
            int opcao = JOptionPane.showConfirmDialog(this, "Deseja exportar o relatorio antes de sair?", "sair", JOptionPane.YES_NO_CANCEL_OPTION);

            if (opcao == JOptionPane.YES_OPTION){
                exportar();
            }
            if (opcao == JOptionPane.CANCEL_OPTION){
                return;
            }
        }
        System.exit(0);
    }
    //getter p controller acessar
    public MedicoesPanel getMedicoesPanel(){
        return medicoesPanel;
    }

    public FiltrosPanel getFiltrosPanel(){
        return filtrosPanel;
    }

    public RegressaoPanel getRegressaoPanel(){
        return regressaoPanel;
    }

    public GraficoPanel getGraficoPanel(){
        return graficoPanel;
    }

    //main
    public static void main(String[] args) {
        new MainFrame();
    }
}