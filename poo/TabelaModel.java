import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TabelaModel extends AbstractTableModel {

    private final String[] colunas = {
            "Timestamp",
            "Cidade",
            "Latitude",
            "Longitude",
            "Temperatura (°C)",
            "Consumo (kWh)",
            "Consumo Previsto",
            "Resíduo (%)"
    };

    private List<Medicao> medicoes;

    public TabelaModel() {
        medicoes = new ArrayList<>();
    }

    public TabelaModel(List<Medicao> medicoes) {
        this.medicoes = medicoes;
    }

    @Override
    public int getRowCount() {
        return medicoes.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Medicao m = medicoes.get(rowIndex);

        switch (columnIndex) {

            case 0:
                return m.getTimestamp();

            case 1:
                return m.getCidade();

            case 2:
                return m.getLatitude();

            case 3:
                return m.getLongitude();

            case 4:
                return m.getTemperatura();

            case 5:
                return m.getConsumoKwh();

            case 6:
                return m.getConsumoPrevisto();

            case 7:
                return m.getResiduoPercentual();

            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {

        return column == 4 || column == 5;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {

        Medicao m = medicoes.get(row);

        try {

            switch (column) {

                case 4:
                    m.setTemperatura(Double.parseDouble(value.toString()));
                    break;

                case 5:
                    m.setConsumoKwh(Double.parseDouble(value.toString()));
                    break;
            }

            fireTableCellUpdated(row, column);

        } catch (NumberFormatException ex) {

            // ignora valor inválido
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {

        switch (column) {

            case 0:
                return java.time.LocalDateTime.class;

            case 1:
                return String.class;

            default:
                return Double.class;
        }
    }

    public void setMedicoes(List<Medicao> lista) {
        this.medicoes = lista;
        fireTableDataChanged();
    }

    public List<Medicao> getMedicoes() {
        return medicoes;
    }

    public Medicao getMedicao(int linha) {
        return medicoes.get(linha);
    }

    public void adicionar(Medicao medicao) {

        medicoes.add(medicao);

        fireTableRowsInserted(
                medicoes.size() - 1,
                medicoes.size() - 1);
    }

    public void remover(int linha) {

        if (linha < 0 || linha >= medicoes.size())
            return;

        medicoes.remove(linha);

        fireTableRowsDeleted(linha, linha);
    }

    public void remover(int[] linhas) {

        for (int i = linhas.length - 1; i >= 0; i--) {

            remover(linhas[i]);
        }
    }

    public void limpar() {

        medicoes.clear();

        fireTableDataChanged();
    }

    public void atualizarLinha(int linha) {

        fireTableRowsUpdated(linha, linha);
    }

    public void atualizarTabela() {

        fireTableDataChanged();
    }
}