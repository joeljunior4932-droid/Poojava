import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import javax.swing.JPanel;

public class GraficoPanel extends JPanel {

    private List<Medicao> medicoes;
    private RegressaoLinear regressao;
    private double limiteOutlier = 20;

    public GraficoPanel() {
        setBackground(Color.WHITE);
    }

    public void atualizar(List<Medicao> medicoes,
                          RegressaoLinear regressao) {

        this.medicoes = medicoes;
        this.regressao = regressao;

        repaint();
    }

    public void setLimiteOutlier(double limite) {
        this.limiteOutlier = limite;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (medicoes == null || medicoes.isEmpty())
            return;

        Graphics2D g2 = (Graphics2D) g;

        int margem = 50;

        int largura = getWidth() - margem * 2;
        int altura = getHeight() - margem * 2;

        g2.drawLine(margem,
                getHeight() - margem,
                getWidth() - margem,
                getHeight() - margem);

        g2.drawLine(margem,
                margem,
                margem,
                getHeight() - margem);

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;

        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (Medicao m : medicoes) {

            minX = Math.min(minX, m.getTemperatura());
            maxX = Math.max(maxX, m.getTemperatura());

            minY = Math.min(minY, m.getConsumoKwh());
            maxY = Math.max(maxY, m.getConsumoKwh());
        }

        for (Medicao m : medicoes) {

            int x = margem +
                    (int)((m.getTemperatura() - minX)
                    / (maxX - minX)
                    * largura);

            int y = getHeight() - margem -
                    (int)((m.getConsumoKwh() - minY)
                    / (maxY - minY)
                    * altura);

            if (Math.abs(m.getResiduoPercentual()) > limiteOutlier)
                g2.setColor(Color.RED);
            else
                g2.setColor(Color.BLACK);

            g2.fillOval(x - 4, y - 4, 8, 8);
        }

        if (regressao != null) {

            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(2));

            double y1 = regressao.preverConsumo(minX);
            double y2 = regressao.preverConsumo(maxX);

            int xInicio = margem;
            int xFim = margem + largura;

            int yy1 = getHeight() - margem -
                    (int)((y1 - minY)
                    / (maxY - minY)
                    * altura);

            int yy2 = getHeight() - margem -
                    (int)((y2 - minY)
                    / (maxY - minY)
                    * altura);

            g2.drawLine(xInicio, yy1, xFim, yy2);

            g2.drawString(
                    "y = "
                    + FormatarNumero.comQuatroCasas(regressao.getBeta0())
                    + " + "
                    + FormatarNumero.comQuatroCasas(regressao.getBeta1())
                    + "x",
                    margem,
                    20);

            g2.drawString(
                    "R² = "
                    + FormatarNumero.comQuatroCasas(regressao.getR2()),
                    margem,
                    40);
        }
    }
}