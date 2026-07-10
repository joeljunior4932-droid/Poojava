import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class OutlierTableCellRenderer extends DefaultTableCellRenderer {

    private double limite;

    public OutlierTableCellRenderer(double limite) {
        this.limite = limite;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        if (isSelected)
            return c;

        TabelaModel model = (TabelaModel) table.getModel();

        Medicao m = model.getMedicao(row);

        double residuo = Math.abs(m.getResiduoPercentual());

        if (residuo > limite) {

            c.setBackground(new Color(255,180,180));

        } else {

            c.setBackground(Color.WHITE);

        }

        return c;
    }
}