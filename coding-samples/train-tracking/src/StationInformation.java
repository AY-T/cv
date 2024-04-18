/* 
 * Class for ...
 */
// Change StationInformation to singular and use as Arraylist?
public class StationInformation {
    // Use API for some or all station information? 
    // https://rata.digitraffic.fi/swagger/#/metadata/getStations

    // LPV-HKI
    private String[] stationId = { "LPV", "MÄK", "PJM", "VMO", "HPL", "KHK", "ILA", "PSL", "HKI"};
    private String[] stationName = { "Leppävaara", "Mäkkylä", "Pitäjänmäki", "Valimo", "Huopalahti", "Helsinki Kivihaka", "Ilmala asema", "Pasila asema", "Helsinki asema"};
    // NOTE: Station with largest distance needs to be in lowest index. Need to change code if otherwise.
    private double[] distance = { 11.2, 9.5, 8.5, 7.5, 6.4, 4.7, 4.4, 3.2, 0.2 };
    private double[] relativePosition = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private double[] relativePositionLabels = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private String firstRouteStation = "LPV";
    private String lastRouteStation = "HKI";
    // public final int size = stationId.length;

/*
    // Alternative set of station and their distances. Comment out previous lines if wanting to try.
    private String[] stationId = { "ÄKI", "SUO", "LAU", "VRI", "JY" };
    private String[] stationName = { "Äänekoski", "Suolahti", "Laukaa", "Vihtavuori", "Jyväskylä"};
    // NOTE: Station with largest distance needs to be in lowest index. Need to change code if otherwise.
    private double[] distance = { 424.5, 417.8, 401.2, 395.2, 377.4};
    private double[] relativePosition = { 0, 0, 0, 0, 0 };
    private String firstRouteStation = "JY";
    private String lastRouteStation = "%C3%84KI";
    public final int size = stationId.length;
 */
    StationInformation() {
        // Calculate the relative position of each station based on distance for later use.
        // NOTE: Distances in this.distance needs to be in decending order!
        for (int i = 0; i < distance.length; i++) {

            // Tested and works: Linear relative distances
            // relativePosition[i] = (((double) i / (double) (distance.length-1)) * 900.0);
            relativePositionLabels[i] = (((double) i / (double) (distance.length)) * 900.0);

            
            // Distances using actual distances (currently graphs only, not labels).
            double largest = distance[0];
            double smallest = distance[this.size()-1];
            relativePosition[i] = (distance[i] - smallest) / (largest - smallest) * 900.0;
        }
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
     * 
     * @param 
     * @return void
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
