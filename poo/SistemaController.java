import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.util.ArrayList;

public class SistemaController {

    private MainFrame mainFrame;

    private MedicaoDAO medicaoDAO;
    private TabelaModel tabelaModel;
    private RegressaoLinear regressao;
    private Filtro filtro;
    private ErroValidacao erros;

    private OutlierTableCellRenderer renderer;
    private List<Medicao> dadosCompletos = new ArrayList<>();

    public SistemaController(MainFrame mainFrame) {

        this.mainFrame = mainFrame;

        medicaoDAO = new MedicaoDAO();
        tabelaModel = new TabelaModel();
        regressao = new RegressaoLinear();
        filtro = new Filtro();
        erros = new ErroValidacao();

        renderer = new OutlierTableCellRenderer(20);
    }

    //=========================
    // GETTERS
    //=========================

    public TabelaModel getTabelaModel() {
        return tabelaModel;
    }

    public RegressaoLinear getRegressao() {
        return regressao;
    }

    public Filtro getFiltro() {
        return filtro;
    }

    public OutlierTableCellRenderer getRenderer() {
        return renderer;
    }

    //=========================
    // CARREGAR CSV
    //=========================

    public void carregarArquivo() {

        JFileChooser chooser = new JFileChooser(".");

        int op = chooser.showOpenDialog(mainFrame);

        if (op != JFileChooser.APPROVE_OPTION)
            return;

        File arquivo = chooser.getSelectedFile();

        erros.resetar();

        try {

            List<Medicao> lista =
                    medicaoDAO.carregar(arquivo, erros);

            dadosCompletos = new ArrayList<>(lista);
            tabelaModel.setMedicoes(lista);

            calcularRegressao();

            atualizarTela();

            JOptionPane.showMessageDialog(
                    mainFrame,
                    erros.getMensagemResumo());

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Erro ao abrir arquivo.");
        }
    }

    //=========================
    // FILTROS
    //=========================

    public void aplicarFiltros() {

        FiltrosPanel painel =
                mainFrame.getFiltrosPanel();

        filtro.setDataInicio(
                painel.getDataInicio());

        filtro.setDataFim(
                painel.getDataFim());

        filtro.setTempMin(
                painel.getTempMin());

        filtro.setTempMax(
                painel.getTempMax());

        filtro.setLatitudeCentro(
                painel.getLatitude());

        filtro.setLongitudeCentro(
                painel.getLongitude());

        filtro.setRaioKm(
                painel.getRaio());

        List<Medicao> filtradas =
                filtro.aplicar(
                        tabelaModel.getMedicoes());
        dadosCompletos = new ArrayList<>(filtradas); //guarda copia dps dos filtros 
        tabelaModel.setMedicoes(filtradas);

        calcularRegressao();

        atualizarTela();
    }

    public void limparFiltros() {

        filtro.limpar();

        mainFrame.getFiltrosPanel().limpar();

        atualizarTela();
    }

    public void exportarTSV() {
        if (tabelaModel.getMedicoes().isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Nenhum dado para exportar.", "aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser(".");
        chooser.setDialogTitle("Exportar relatorio TSV");

        int op = chooser.showSaveDialog(mainFrame);

        if (op != JFileChooser.APPROVE_OPTION)
            return;

        File arquivo = chooser.getSelectedFile();

        try{
            java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(arquivo));

            //cabecalho
            pw.println("timestamp\tcidade\tlatitude\tlongitude\t" + "temperatura\tconsumoKwh\tconsumoPrevisto\tresiduoPercentual");
            //dados 
            for (Medicao m : tabelaModel.getMedicoes()) {
                pw.println(m.getTimestamp() + "\t" +
                    m.getCidade() + "\t" +
                    m.getLatitude() + "\t" +
                    m.getLongitude() + "\t" +
                    m.getTemperatura() + "\t" +
                    m.getConsumoKwh() + "\t" +
                    m.getConsumoPrevisto() + "\t" +
                    m.getResiduoPercentual()
                );
            }

            pw.close();

            JOptionPane.showMessageDialog(mainFrame, "relatorio exportado c sucesso", "exportacao", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "erro ao exportar arquivo", "erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    //=========================
    // REGRESSÃO
    //=========================

    public void calcularRegressao() {

        if (!regressao.calcular(
                tabelaModel.getMedicoes())) {

            mainFrame.getRegressaoPanel()
                    .limpar();

            if (!tabelaModel.getMedicoes().isEmpty()){
                JOptionPane.showMessageDialog(mainFrame, "sao necessarias pelo menos duas medicoes para calcular a regressao.",
                    "aviso", JOptionPane.WARNING_MESSAGE);
            }

            return;
        }

        medicaoDAO.atualizarPrevisoes(
                tabelaModel.getMedicoes(),
                regressao);

        mainFrame.getRegressaoPanel()
                .atualizar(regressao);
    }

    //adicionar linha 
    public void adicionarMedicao() {
        try {
            String timestamp = JOptionPane.showInputDialog(mainFrame, "Timestamp (yyyy-MM-dd HH:mm:ss):");
            if (timestamp == null) return;

            String cidade = JOptionPane.showInputDialog(mainFrame, "Cidade: ):");
            if (cidade == null) return;

            String lat = JOptionPane.showInputDialog(mainFrame, "Latitude: ):");
            if (lat == null) return;

            String longit = JOptionPane.showInputDialog(mainFrame, "Longtitude: ):");
            if (longit == null) return;

            String temp = JOptionPane.showInputDialog(mainFrame, "Temperatura(ºC): ):");
            if (temp == null) return;

            String consumo = JOptionPane.showInputDialog(mainFrame, "Consumo(kWh): ):");
            if (consumo == null) return;

            java.time.LocalDateTime ts = java.time.LocalDateTime.parse(timestamp, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            

            Medicao m = new Medicao(ts, cidade, Double.parseDouble(lat), Double.parseDouble(longit), Double.parseDouble(temp), Double.parseDouble(consumo));
            tabelaModel.adicionar(m);
            calcularRegressao();
            atualizarTela();
        } 
        catch(Exception e) {
            JOptionPane.showMessageDialog(mainFrame, "Dados inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    //=========================
    // TABELA
    //=========================

    public void atualizarTabela() {

        tabelaModel.fireTableDataChanged();
    }

    public void adicionarMedicao(
            Medicao medicao) {

        tabelaModel.adicionar(medicao);

        calcularRegressao();

        atualizarTela();
    }

    public void removerSelecionadas() {

        mainFrame.getMedicoesPanel()
                 .removerSelecionadas();

        calcularRegressao();

        atualizarTela();
    }

    public void limparTabela() {

        tabelaModel.limpar();

        mainFrame.getRegressaoPanel()
                 .limpar();

        atualizarTela();
    }

    //=========================
    // OUTLIERS
    //=========================

    public void alterarLimiteOutlier(
            double limite) {

        renderer.setLimite(limite);

        mainFrame.getGraficoPanel()
                 .setLimiteOutlier(limite);

        atualizarTela();
    }

    //=========================
    // ATUALIZA TODA A INTERFACE
    //=========================

    public void atualizarTela() {

        atualizarTabela();

        mainFrame.getGraficoPanel()
                .atualizar(
                        tabelaModel.getMedicoes(),
                        regressao);

        mainFrame.getMedicoesPanel()
                .atualizarTabela();
    }

    //outliers com exclusao 
    public void aplicarExclusaoOutliers(int limitePercent, boolean excluir) {
        double limite = limitePercent;
        
        renderer.setLimite(limite);
        mainFrame.getGraficoPanel().setLimiteOutlier(limite);

        if (excluir) {
            //filtra - so mantem oq n eh outl
            List<Medicao> semOutliers = new ArrayList<>();

            for (Medicao m : dadosCompletos) {
                if (Math.abs(m.getResiduoPercentual()) <= limite) {
                    semOutliers.add(m);
                }
            }

            tabelaModel.setMedicoes(semOutliers);
        } else {
            //motra tds
            tabelaModel.setMedicoes(new ArrayList<>(dadosCompletos));
        }

        calcularRegressao();
        atualizarTela();
    }
} 