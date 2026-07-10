import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MedicaoDAO {

    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MedicaoDAO() {
    }

    public List<Medicao> carregar(File arquivo,
                                  ErroValidacao erros)
            throws IOException {

        List<Medicao> lista = new ArrayList<>();

        BufferedReader br = new BufferedReader(
                new FileReader(arquivo));

        String linha;

        br.readLine(); // cabeçalho

        while ((linha = br.readLine()) != null) {

            erros.incrementarLinhaProcessada();

            try {

                Medicao medicao = criarMedicao(linha);

                if (validar(medicao)) {

                    lista.add(medicao);
                    erros.incrementarLinhaValida();

                } else {

                    registrarErro(erros, medicao);
                }

            } catch (Exception e) {

                erros.incrementarErroFormato();
            }

        }

        br.close();

        return lista;
    }

    private Medicao criarMedicao(String linha) {

        String[] partes = linha.trim().split("\t");

        LocalDateTime timestamp =
                LocalDateTime.parse(partes[0], FORMATO);

        String cidade = partes[1];

        double latitude =
                Double.parseDouble(partes[2]);

        double longitude =
                Double.parseDouble(partes[3]);

        double temperatura =
                Double.parseDouble(partes[4]);

        double consumo =
                Double.parseDouble(partes[5]);

        return new Medicao(
                timestamp,
                cidade,
                latitude,
                longitude,
                temperatura,
                consumo);
    }

    public boolean validar(Medicao m) {

        if (m.getLatitude() < -90 ||
                m.getLatitude() > 90)
            return false;

        if (m.getLongitude() < -180 ||
                m.getLongitude() > 180)
            return false;

        if (m.getTemperatura() < -50 ||
                m.getTemperatura() > 60)
            return false;

        if (m.getConsumoKwh() < 0)
            return false;

        return true;
    }

    private void registrarErro(ErroValidacao erros,
                               Medicao m) {

        if (m.getLatitude() < -90 ||
                m.getLatitude() > 90 ||
                m.getLongitude() < -180 ||
                m.getLongitude() > 180) {

            erros.incrementarErroCoordenada();
        }

        if (m.getTemperatura() < -50 ||
                m.getTemperatura() > 60) {

            erros.incrementarErroTemperatura();
        }

        if (m.getConsumoKwh() < 0) {

            erros.incrementarErroConsumo();
        }
    }

    public void atualizarPrevisoes(List<Medicao> lista,
                                   RegressaoLinear regressao) {

        for (Medicao m : lista) {

            double previsto =
                    regressao.preverConsumo(
                            m.getTemperatura());

            m.setConsumoPrevisto(previsto);

            double residuo =
                    ((m.getConsumoKwh() - previsto)
                            / previsto) * 100.0;

            m.setResiduoPercentual(residuo);
        }
    }
}