import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import api.GetInformartionFromApi;


public class TrainSchedulesFromJson {
    // private final String[] validStations = { "ÄKI", "SUO", "LAU", "VRI", "JY" };
    private StationInformation stationInfo;

    TrainSchedulesFromJson(StationInformation stationInfo) {
        this.stationInfo = stationInfo;
    }


    public ArrayList<TrainInformation> run(String station1, String station2) {

        Random rand = new Random();
        
        GetInformartionFromApi api = new GetInformartionFromApi();
        JSONArray json = api.getTrainInfoFromApi(station1, station2);
        
        ArrayList<TrainInformation> trains = new ArrayList<TrainInformation>();
        // int trainIterator = 0;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");        

        final LocalDateTime timeNow = LocalDateTime.now();
        final LocalDateTime timeNowMinusXHours = timeNow.minusHours(1);
        final LocalDateTime timeNowPlusXHours = timeNow.plusHours(1);      
        // final LocalDateTime timeNowMinusXHours = timeNow.minusMinutes(30);
        // final LocalDateTime timeNowPlusXHours = timeNow.plusMinutes(30);  

        for (Object train : json) {
            boolean includeThisTrain = true;

            JSONObject jsontrain = (JSONObject) train;
            String trainNumber = jsontrain.get("trainNumber").toString();
            
            // Current implementation, where train is added immediately.
            // !! trains.add(new TrainInformation(trainName));

            // New implementation. Potential train candidate to be added.
            TrainInformation potentialTrain = new TrainInformation();
            potentialTrain.setTrainNumber(trainNumber);



            JSONArray timetable = (JSONArray) jsontrain.get("timeTableRows");

            // Used to limit what trains are actually added (only those relatively close to the current time)
            // LocalDateTime earliestDateTime = LocalDateTime.parse("3000-01-01T00:00:00.001Z", formatter);
            // LocalDateTime latestDateTime = LocalDateTime.parse("1900-01-01T00:00:00.001Z", formatter);

            // Parse out important timetable information from JSON.
            for (Object timetableRow : timetable) {
                JSONObject jsonTimeTableRow = (JSONObject) timetableRow;
                LocalDateTime scheduledTime = LocalDateTime.parse((String)jsonTimeTableRow.get("scheduledTime"), formatter);
                LocalDateTime actualTime;
                if (jsonTimeTableRow.get("actualTime") == null) {
                    actualTime = null;
                } else {
                    actualTime = LocalDateTime.parse((String)jsonTimeTableRow.get("actualTime"), formatter);
                }

                // No not include trains that have a timestamp > +-X hours from current time.
                if (scheduledTime.isAfter(timeNowPlusXHours) || scheduledTime.isBefore(timeNowMinusXHours) ) {
                    includeThisTrain = false;
                    // System.out.println("Not included: " + scheduledTime + " vs. " + timeNow);
                } else {
                    // System.out.println("INCLUDED: " + scheduledTime + " vs. " + timeNow);
                }

                String stationShortCode = (String) jsonTimeTableRow.get("stationShortCode");
                String stopType = (String) jsonTimeTableRow.get("type");


                // We're only interested in station information for a specific route,
                // represented by valid stations.
                boolean ValidStation = false;

                for (int i = 0; i < stationInfo.size; i++) {
                    if (stationShortCode.equals(stationInfo.getStationId(i))) {

                        TimeTableRow newRow = new TimeTableRow(scheduledTime, actualTime, stationShortCode, stopType);
                        
                        // Current implementation: Add straigth to ArrayList
                        // !! trains.get(trainIterator).addToTimeTable(newRow);

                        // New implementation: Add to potentialTrain
                        potentialTrain.addToTimeTable(newRow);

                        ValidStation = true;
                    }
                }
                if (ValidStation == false) {
                    // System.out.println("Not a valid station: " + stationShortCode);
                }

            }
            
            // TODO: Add conditions below when a train is not added to Arraylist. (e.g. too far from timoNow).

/*             System.out.println("======");
            System.out.println(potentialTrain.getTrainName());
            System.out.println(trains.get(trainIterator).getTrainName());
 */            

            if (includeThisTrain == true) {
                // Add a random color to the train about to be added.
                int red   = (int)(255 * rand.nextFloat());
                int green = (int)(255 * rand.nextFloat());
                int blue  = (int)(255 * rand.nextFloat());
                potentialTrain.trainColor = new Color(red, green, blue);

                trains.add(potentialTrain);

    
                // trainIterator++;
            }
            
        }



        return trains;

    }  
}
