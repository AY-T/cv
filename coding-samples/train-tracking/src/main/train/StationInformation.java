package train;

/* 
 * Class for storing information regarding train stations, and their relative distances.
 */
public class StationInformation {
    private String[] stationId;
    private String[] stationName;
    private double[] distance;
    private double[] relativePosition;
    private double[] relativePositionLabels;
    private String firstRouteStation;
    private String lastRouteStation;

    /**
     * Calculate the relative position of each station based on distance for later
     * use.
     * Called by constructors.
     * NOTE: Once only a single dynamic constructor, this method become obsolete.
     */
    private void calculateRelativePositions() {

        // NOTE: Distances in this.distance needs to be in decending order!
        for (int i = 0; i < distance.length; i++) {

            // Distances using actual distances
            final double largest = distance[0];
            final double smallest = distance[this.size() - 1];
            this.relativePosition[i] = (distance[i] - smallest) / (largest - smallest) * 900.0;

            // Tested and works: Linear relative distances
            // relativePosition[i] = (((double) i / (double) (distance.length-1)) * 900.0);
            this.relativePositionLabels[i] = (distance[i] - smallest) / (largest - smallest) * 900.0;
        }
    }

    /**
     * Default constructor for trains running between Leppävaara and Helsinki Asema
     */
    public StationInformation() {
        stationId = new String[] { "LPV", "MÄK", "PJM", "VMO", "HPL", "KHK", "ILA", "PSL", "HKI" };
        stationName = new String[] { "Leppävaara", "Mäkkylä", "Pitäjänmäki", "Valimo", "Huopalahti",
                "Helsinki Kivihaka", "Ilmala asema", "Pasila asema", "Helsinki asema" };
        // NOTE: Station with largest distance needs to be in lowest index.
        distance = new double[] { 11.2, 9.5, 8.5, 7.5, 6.4, 4.7, 4.4, 3.2, 0.2 };
        relativePosition = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        relativePositionLabels = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        firstRouteStation = "LPV";
        lastRouteStation = "HKI";

        calculateRelativePositions();
    }

    /**
     * Alternative constructor meant for trains running between other stations.
     */
    public StationInformation(String route) {
        // Currently only implemented for the being called with "JKÄKI"
        if (route.equals("JKÄKI")) {
            stationId = new String[] { "ÄKI", "SUO", "LAU", "VRI", "JK" };
            stationName = new String[] { "Äänekoski", "Suolahti", "Laukaa",
                    "Vihtavuori", "Jyväskylä" };
            // NOTE: Station with largest distance needs to be in lowest index.
            distance = new double[] { 424.5, 417.8, 401.2, 395.2, 377.4 };
            relativePosition = new double[] { 0, 0, 0, 0, 0 };
            relativePositionLabels = new double[] { 0, 0, 0, 0, 0 };
            firstRouteStation = "JY";
            lastRouteStation = "%C3%84KI";
        }

        calculateRelativePositions();
    }

    public int size() {
        return distance.length;
    }

    public double getRelativePosition(int index) {
        return relativePosition[index];
    }

    public double getRelativePositionLabels(int index) {
        return relativePositionLabels[index];
    }

    public String getStation1() {
        return this.firstRouteStation;
    }

    public String getStation2() {
        return this.lastRouteStation;
    }

    public String getStationId(int index) {
        return stationId[index];
    }

    public String getStationName(int index) {
        return stationName[index];
    }

    public double getDistance(int index) {
        return distance[index];
    }

    /**
     * Returns the index of a station being searched for.
     * 
     * @param String searchId: Two or three letter short code for station who's
     *               index is needed.
     * @return Index of matching station. Return -1 if not found.
     */
    public int getIndexForStationId(String searchId) {
        for (int i = 0; i < this.size(); i++) {
            if (this.stationId[i].equals(searchId)) {
                return i;
            }
        }
        return -1;
    }

}
