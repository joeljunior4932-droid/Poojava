import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Filtro {

    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

    private Double tempMin;
    private Double tempMax;

    private Double latitudeCentro;
    private Double longitudeCentro;
    private Double raioKm;

    public Filtro() {
        limpar();
    }

    public List<Medicao> aplicar(List<Medicao> dados) {

        List<Medicao> resultado = aplicarFiltroTempo(dados);
        resultado = aplicarFiltroTemperatura(resultado);
        resultado = aplicarFiltroRaio(resultado);

        return resultado;
    }

    public List<Medicao> aplicarFiltroTempo(List<Medicao> dados) {

        List<Medicao> lista = new ArrayList<>();

        for (Medicao m : dados) {

            if (dataInicio != null &&
                m.getTimestamp().isBefore(dataInicio))
                continue;

            if (dataFim != null &&
                m.getTimestamp().isAfter(dataFim))
                continue;

            lista.add(m);
        }

        return lista;
    }

    public List<Medicao> aplicarFiltroTemperatura(List<Medicao> dados) {

        List<Medicao> lista = new ArrayList<>();

        for (Medicao m : dados) {

            if (tempMin != null &&
                m.getTemperatura() < tempMin)
                continue;

            if (tempMax != null &&
                m.getTemperatura() > tempMax)
                continue;

            lista.add(m);
        }

        return lista;
    }

    public List<Medicao> aplicarFiltroRaio(List<Medicao> dados) {

        if (latitudeCentro == null ||
            longitudeCentro == null ||
            raioKm == null)
            return dados;

        List<Medicao> lista = new ArrayList<>();

        for (Medicao m : dados) {

            double distancia = calcularDistancia(
                    latitudeCentro,
                    longitudeCentro,
                    m.getLatitude(),
                    m.getLongitude());

            if (distancia <= raioKm)
                lista.add(m);
        }

        return lista;
    }

    public void limpar() {

        dataInicio = null;
        dataFim = null;

        tempMin = null;
        tempMax = null;

        latitudeCentro = null;
        longitudeCentro = null;
        raioKm = null;
    }

    public boolean estaAtivo() {

        return dataInicio != null ||
               dataFim != null ||
               tempMin != null ||
               tempMax != null ||
               latitudeCentro != null;
    }

    public double calcularDistancia(double lat1,
                                    double lon1,
                                    double lat2,
                                    double lon2) {

        return CalculoDistancia.calcular(
                lat1, lon1, lat2, lon2);
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }

    public Double getLatitudeCentro() {
        return latitudeCentro;
    }

    public void setLatitudeCentro(Double latitudeCentro) {
        this.latitudeCentro = latitudeCentro;
    }

    public Double getLongitudeCentro() {
        return longitudeCentro;
    }

    public void setLongitudeCentro(Double longitudeCentro) {
        this.longitudeCentro = longitudeCentro;
    }

    public Double getRaioKm() {
        return raioKm;
    }

    public void setRaioKm(Double raioKm) {
        this.raioKm = raioKm;
    }
}