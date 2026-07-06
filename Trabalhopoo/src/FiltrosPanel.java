import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

public class FiltrosPanel extends JPanel {

    private JSpinner spInicio;
    private JSpinner spFim;

    private JTextField txtTempMin;
    private JTextField txtTempMax;

    private JTextField txtLatitude;
    private JTextField txtLongitude;
    private JTextField txtRaio;

    private JButton btnAplicar;
    private JButton btnLimpar;

    public FiltrosPanel() {

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        spInicio = new JSpinner(new SpinnerDateModel());
        spFim = new JSpinner(new SpinnerDateModel());

        txtTempMin = new JTextField(8);
        txtTempMax = new JTextField(8);

        txtLatitude = new JTextField(10);
        txtLongitude = new JTextField(10);
        txtRaio = new JTextField(10);

        btnAplicar = new JButton("Aplicar");
        btnLimpar = new JButton("Limpar");

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Data Inicial"), gbc);

        gbc.gridx = 1;
        add(spInicio, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Data Final"), gbc);

        gbc.gridx = 1;
        add(spFim, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Temperatura Mínima"), gbc);

        gbc.gridx = 1;
        add(txtTempMin, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Temperatura Máxima"), gbc);

        gbc.gridx = 1;
        add(txtTempMax, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Latitude"), gbc);

        gbc.gridx = 1;
        add(txtLatitude, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Longitude"), gbc);

        gbc.gridx = 1;
        add(txtLongitude, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Raio (km)"), gbc);

        gbc.gridx = 1;
        add(txtRaio, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(btnAplicar, gbc);

        gbc.gridx = 1;
        add(btnLimpar, gbc);
    }

    public LocalDateTime getDataInicio() {
        Date d = (Date) spInicio.getValue();
        return d.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public LocalDateTime getDataFim() {
        Date d = (Date) spFim.getValue();
        return d.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public Double getTempMin() {

        if (txtTempMin.getText().isBlank())
            return null;

        return Double.parseDouble(txtTempMin.getText());
    }

    public Double getTempMax() {

        if (txtTempMax.getText().isBlank())
            return null;

        return Double.parseDouble(txtTempMax.getText());
    }

    public Double getLatitude() {

        if (txtLatitude.getText().isBlank())
            return null;

        return Double.parseDouble(txtLatitude.getText());
    }

    public Double getLongitude() {

        if (txtLongitude.getText().isBlank())
            return null;

        return Double.parseDouble(txtLongitude.getText());
    }

    public Double getRaio() {

        if (txtRaio.getText().isBlank())
            return null;

        return Double.parseDouble(txtRaio.getText());
    }

    public JButton getBtnAplicar() {
        return btnAplicar;
    }

    public JButton getBtnLimpar() {
        return btnLimpar;
    }

    public void limpar() {

        txtTempMin.setText("");
        txtTempMax.setText("");

        txtLatitude.setText("");
        txtLongitude.setText("");
        txtRaio.setText("");
    }
}