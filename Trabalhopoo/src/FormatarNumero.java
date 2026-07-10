import java.text.DecimalFormat;

public class FormatarNumero {

    private static final DecimalFormat QUATRO =
            new DecimalFormat("0.0000");

    private static final DecimalFormat PERCENTUAL =
            new DecimalFormat("0.00");

    public static String comQuatroCasas(double valor) {
        return QUATRO.format(valor);
    }

    public static String comPercentual(double valor) {
        return PERCENTUAL.format(valor) + "%";
    }
}