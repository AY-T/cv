package api;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import train.StationInformation;
import train.TimeTableRow;
import train.TrainInformation;
import util.Colors;

/* 
 * Class for gathering/parsing necessary train schedule information to be used by other classes.
 */
public class TrainSchedulesFromJson {
    private StationInformation stationInfo;

    public TrainSchedulesFromJson(StationInformation stationInfo) {
        this.stationInfo = stationInfo;
    }

    /**
     * Gathers/parses necessary train and train schedule information.
     * 
     * @param String station1: Two or three letter station code for other end of
     *               used train route.
     * @param String station2: Two or three letter station code for other end of
     *               used train route.
     * @param int gapHours: Include trains with entries +-2 hours from current time.
     * @return ArrayList<TrainInformation>: gathered train/train schedule
     *         information.
     */
    public ArrayList<TrainInformation> run(String station1, String station2, int gapHours) {

        GetInformartionFromApi api = new GetInformartionFromApi();
        JSONArray json = api.getTrainInfoFromApi(station1, station2);

        ArrayList<TrainInformation> trains = new ArrayList<TrainInformation>();
        // int trainIterator = 0;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        final LocalDateTime timeNow = LocalDateTime.now();
        final LocalDateTime timeNowMinusXHours = timeNow.minusHours(gapHours);
        final LocalDateTime timeNowPlusXHours = timeNow.plusHours(gapHours);
        // final LocalDateTime timeNowMinusXHours = timeNow.minusMinutes(30);
        // final LocalDateTime timeNowPlusXHours = timeNow.plusMinutes(30);

        for (Object train : json) {
            boolean includeThisTrain = true;

            JSONObject jsontrain = (JSONObject) train;
            String trainNumber = jsontrain.get("trainNumber").toString();

            // Potential train candidate to be added. Only added if within given time limit
            // from current time.
            TrainInformation potentialTrain = new TrainInformation();
            potentialTrain.setTrainNumber(trainNumber);

            JSONArray timetable = (JSONArray) jsontrain.get("timeTableRows");

            // Parse out important timetable information from JSON.
            for (Object timetableRow : timetable) {
                JSONObject jsonTimeTableRow = (JSONObject) timetableRow;
                LocalDateTime scheduledTime = LocalDateTime.parse((String) jsonTimeTableRow.get("scheduledTime"),
                        formatter);
                LocalDateTime actualTime;
                if (jsonTimeTableRow.get("actualTime") == null) {
                    actualTime = null;
                } else {
                    actualTime = LocalDateTime.parse((String) jsonTimeTableRow.get("actualTime"), formatter);
                }

                // No not include trains that have a timestamp > +-X hours from current time.
                if (scheduledTime.isAfter(timeNowPlusXHours) || scheduledTime.isBefore(timeNowMinusXHours)) {
                    includeThisTrain = false;
                }
                String stationShortCode = (String) jsonTimeTableRow.get("stationShortCode");
                String stopType = (String) jsonTimeTableRow.get("type");
                // Do we need trainStopping ?

                // We're only interested in station information for a specific route,
                // represented by valid stations.

                // boolean ValidStation = false;

                for (int i = 0; i < stationInfo.size(); i++) {
                    if (stationShortCode.equals(stationInfo.getStationId(i))) {

                        TimeTableRow newRow = new TimeTableRow(scheduledTime, actualTime, stationShortCode, stopType);

                        potentialTrain.addToTimeTable(newRow);

                        // ValidStation = true;
                    }
                }

                // For debugging only. No valid station identified.
                // if (ValidStation == false );
                //
                // }
            }

            if (includeThisTrain == true) {
                // Assign a random color to the train about to be added.
                potentialTrain.setTrainColor(Colors.randomColor());

                trains.add(potentialTrain);

                // trainIterator++;

            }
        }

        return trains;
    }
}
