public class ErroValidacao {

    private int errosCoordenada;
    private int errosTemperatura;
    private int errosConsumo;
    private int errosFormato;
    private int linhasProcessadas;
    private int linhasValidas;

    public void incrementarErroCoordenada() {
        errosCoordenada++;
    }

    public void incrementarErroTemperatura() {
        errosTemperatura++;
    }

    public void incrementarErroConsumo() {
        errosConsumo++;
    }

    public void incrementarErroFormato() {
        errosFormato++;
    }

    public void incrementarLinhaProcessada() {
        linhasProcessadas++;
    }

    public void incrementarLinhaValida() {
        linhasValidas++;
    }

    public String getMensagemResumo() {

        return "Linhas processadas: " + linhasProcessadas +
                "\nLinhas válidas: " + linhasValidas +
                "\nErros de coordenada: " + errosCoordenada +
                "\nErros de temperatura: " + errosTemperatura +
                "\nErros de consumo: " + errosConsumo +
                "\nErros de formato: " + errosFormato;
    }

    public void resetar() {
        errosCoordenada = 0;
        errosTemperatura = 0;
        errosConsumo = 0;
        errosFormato = 0;
        linhasProcessadas = 0;
        linhasValidas = 0;
    }
}