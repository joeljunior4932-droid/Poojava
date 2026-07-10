public class CalculoDistancia {

    private static final double RAIO_TERRA = 6371.0;

    public static double calcular(double lat1,
                                  double lon1,
                                  double lat2,
                                  double lon2) {

        double dLat = toRadians(lat2 - lat1);
        double dLon = toRadians(lon2 - lon1);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(toRadians(lat1))
                * Math.cos(toRadians(lat2))
                * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RAIO_TERRA * c;
    }

    private static double toRadians(double degree) {
        return Math.toRadians(degree);
    }
}