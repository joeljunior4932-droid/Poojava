import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class RegressaoPanel extends JPanel {

    private JLabel lblBeta0;
    private JLabel lblBeta1;
    private JLabel lblR2;
    private JLabel lblQuantidade;

    private JProgressBar barraR2;

    public RegressaoPanel() {

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.anchor = GridBagConstraints.WEST;

        lblBeta0 = new JLabel("β0: 0.0000");
        lblBeta1 = new JLabel("β1: 0.0000");
        lblR2 = new JLabel("R²: 0.0000");
        lblQuantidade = new JLabel("Medições: 0");

        barraR2 = new JProgressBar(0,100);
        barraR2.setStringPainted(true);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblBeta0, gbc);

        gbc.gridy++;
        add(lblBeta1, gbc);

        gbc.gridy++;
        add(lblR2, gbc);

        gbc.gridy++;
        add(lblQuantidade, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(barraR2, gbc);
    }

    public void atualizar(RegressaoLinear regressao) {

        lblBeta0.setText(
                "β0: " +
                FormatarNumero.comQuatroCasas(regressao.getBeta0()));

        lblBeta1.setText(
                "β1: " +
                FormatarNumero.comQuatroCasas(regressao.getBeta1()));

        lblR2.setText(
                "R²: " +
                FormatarNumero.comQuatroCasas(regressao.getR2()));

        lblQuantidade.setText(
                "Medições: " +
                regressao.getN());

        int valor = (int) (regressao.getR2() * 100);

        barraR2.setValue(valor);
        barraR2.setString(valor + "%");
    }

    public void limpar() {

        lblBeta0.setText("β0: 0.0000");
        lblBeta1.setText("β1: 0.0000");
        lblR2.setText("R²: 0.0000");
        lblQuantidade.setText("Medições: 0");

        barraR2.setValue(0);
        barraR2.setString("0%");
    }

    public JProgressBar getBarraR2() {
        return barraR2;
    }

    public JLabel getLblBeta0() {
        return lblBeta0;
    }

    public JLabel getLblBeta1() {
        return lblBeta1;
    }

    public JLabel getLblR2() {
        return lblR2;
    }

    public JLabel getLblQuantidade() {
        return lblQuantidade;
    }
}