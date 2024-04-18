import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import api.GetInformartionFromApi;


/* 
 * Class for ...
 */
public class TrainSchedulesFromJson {
    private StationInformation stationInfo;

    TrainSchedulesFromJson(StationInformation stationInfo) {
        this.stationInfo = stationInfo;
    }

    /** 
     * 
     * @param 
     * @return void
     */
    public ArrayList<TrainInformation> run(String station1, String station2) {

        Random rand = new Random();
        
        GetInformartionFromApi api = new GetInformartionFromApi();
        JSONArray json = api.getTrainInfoFromApi(station1, station2);
        
        ArrayList<TrainInformation> trains = new ArrayList<TrainInformation>();
        // int trainIterator = 0;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");        

        final LocalDateTime timeNow = LocalDateTime.now();
        final LocalDateTime timeNowMinusXHours = timeNow.minusHours(4);
        final LocalDateTime timeNowPlusXHours = timeNow.plusHours(4);      
        // final LocalDateTime timeNowMinusXHours = timeNow.minusMinutes(30);
        // final LocalDateTime timeNowPlusXHours = timeNow.plusMinutes(30);  

        for (Object train : json) {
            boolean includeThisTrain = true;

            JSONObject jsontrain = (JSONObject) train;
            String trainNumber = jsontrain.get("trainNumber").toString();

            // New implementation. Potential train candidate to be added.
            TrainInformation potentialTrain = new TrainInformation();
            potentialTrain.setTrainNumber(trainNumber);

            JSONArray timetable = (JSONArray) jsontrain.get("timeTableRows");

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
                }
                String stationShortCode = (String) jsonTimeTableRow.get("stationShortCode");
                String stopType = (String) jsonTimeTableRow.get("type");
                // Do we need trainStopping ?

                // We're only interested in station information for a specific route,
                // represented by valid stations.
                boolean ValidStation = false;

                for (int i = 0; i < stationInfo.size(); i++) {
                    if (stationShortCode.equals(stationInfo.getStationId(i))) {

                        TimeTableRow newRow = new TimeTableRow(scheduledTime, actualTime, stationShortCode, stopType);

                        // New implementation: Add to potentialTrain
                        potentialTrain.addToTimeTable(newRow);

                        ValidStation = true;
                    }
                }
                if (ValidStation == false) {
                    // System.out.println("Not a valid station: " + stationShortCode);
                }

            }
        
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
