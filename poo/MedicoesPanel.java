import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MedicoesPanel extends JPanel {

    private JTable tabela;
    private TabelaModel tabelaModel;

    private JButton btnAdicionar;
    private JButton btnRemover;

    public MedicoesPanel() {

        setLayout(new BorderLayout());

        tabelaModel = new TabelaModel();

        tabela = new JTable(tabelaModel);

        tabela.setFillsViewportHeight(true);
        tabela.setAutoCreateRowSorter(true);

        JScrollPane scroll = new JScrollPane(tabela);

        add(scroll, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));

        btnAdicionar = new JButton("Adicionar Linha");
        btnRemover = new JButton("Remover Linha");

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnRemover);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    public void setTabelaModel (TabelaModel model) {
        this.tabelaModel = model;
        tabela.setModel(model);
    }

    public JTable getTabela() {
        return tabela;
    }

    public TabelaModel getTabelaModel() {
        return tabelaModel;
    }

    public JButton getBtnAdicionar() {
        return btnAdicionar;
    }

    public JButton getBtnRemover() {
        return btnRemover;
    }

    public void atualizarTabela() {
        tabelaModel.fireTableDataChanged();
    }

    public void adicionarMedicao(Medicao medicao) {
        tabelaModel.adicionar(medicao);
    }

    public void removerSelecionadas() {

        int[] linhas = tabela.getSelectedRows();

        if (linhas.length == 0)
            return;

        tabelaModel.remover(linhas);
    }

    public void limparTabela() {
        tabelaModel.limpar();
    }
}