import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class SistemaController {

    private MainFrame mainFrame;

    private MedicaoDAO medicaoDAO;
    private TabelaModel tabelaModel;
    private RegressaoLinear regressao;
    private Filtro filtro;
    private ErroValidacao erros;

    private OutlierTableCellRenderer renderer;

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

        JFileChooser chooser = new JFileChooser();

        int op = chooser.showOpenDialog(mainFrame);

        if (op != JFileChooser.APPROVE_OPTION)
            return;

        File arquivo = chooser.getSelectedFile();

        erros.resetar();

        try {

            List<Medicao> lista =
                    medicaoDAO.carregar(arquivo, erros);

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

        tabelaModel.setMedicoes(filtradas);

        calcularRegressao();

        atualizarTela();
    }

    public void limparFiltros() {

        filtro.limpar();

        mainFrame.getFiltrosPanel().limpar();

        atualizarTela();
    }

    //=========================
    // REGRESSÃO
    //=========================

    public void calcularRegressao() {

        if (!regressao.calcular(
                tabelaModel.getMedicoes())) {

            mainFrame.getRegressaoPanel()
                    .limpar();

            return;
        }

        medicaoDAO.atualizarPrevisoes(
                tabelaModel.getMedicoes(),
                regressao);

        mainFrame.getRegressaoPanel()
                .atualizar(regressao);
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
}