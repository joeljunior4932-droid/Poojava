import java.util.List;

public class RegressaoLinear {

    private double beta0;
    private double beta1;
    private double r2;

    private int n;

    private List<Medicao> dados;

    public RegressaoLinear() {
    }

    public boolean calcular(List<Medicao> dados) {

        if (dados == null || dados.size() < 2)
            return false;

        this.dados = dados;
        this.n = dados.size();

        calcularBeta0();
        calcularBeta1();
        calcularR2();
        calcularResiduos();

        return true;
    }

    private void calcularBeta0() {

        double somaX = 0;
        double somaY = 0;

        for (Medicao m : dados) {

            somaX += m.getTemperatura();
            somaY += m.getConsumoKwh();
        }

        double mediaX = somaX / n;
        double mediaY = somaY / n;

        beta0 = mediaY - beta1 * mediaX;
    }

    private void calcularBeta1() {

        double somaX = 0;
        double somaY = 0;

        for (Medicao m : dados) {

            somaX += m.getTemperatura();
            somaY += m.getConsumoKwh();
        }

        double mediaX = somaX / n;
        double mediaY = somaY / n;

        double numerador = 0;
        double denominador = 0;

        for (Medicao m : dados) {

            double dx = m.getTemperatura() - mediaX;
            double dy = m.getConsumoKwh() - mediaY;

            numerador += dx * dy;
            denominador += dx * dx;
        }

        beta1 = numerador / denominador;

        beta0 = mediaY - beta1 * mediaX;
    }

    private void calcularR2() {

        double media = 0;

        for (Medicao m : dados)
            media += m.getConsumoKwh();

        media /= n;

        double sqr = 0;
        double sqt = 0;

        for (Medicao m : dados) {

            double previsto = preverConsumo(
                    m.getTemperatura());

            sqr += Math.pow(
                    m.getConsumoKwh() - previsto,
                    2);

            sqt += Math.pow(
                    m.getConsumoKwh() - media,
                    2);
        }

        r2 = 1 - sqr / sqt;
    }

    public double preverConsumo(double temperatura) {

        return beta0 + beta1 * temperatura;
    }

    private void calcularResiduos() {

        for (Medicao m : dados) {

            double previsto =
                    preverConsumo(m.getTemperatura());

            m.setConsumoPrevisto(previsto);

            double residuo =
                    ((m.getConsumoKwh() - previsto)
                    / previsto) * 100;

            m.setResiduoPercentual(residuo);
        }
    }

    public double getBeta0() {
        return beta0;
    }

    public double getBeta1() {
        return beta1;
    }

    public double getR2() {
        return r2;
    }

    public int getN() {
        return n;
    }
}