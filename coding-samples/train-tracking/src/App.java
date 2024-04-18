import java.time.LocalDateTime;
import java.util.ArrayList;

/* 
 * Class for ...
 */
public class App {

    // TODO: Deploy somewhere? 

    // TODO: Make as live train following instead of static picture of when run.
    public static void run() {
        // NOTE: Used route and station hard coded into StationInformation.
        StationInformation stationInfo = new StationInformation();

        TrainSchedulesFromJson trainSchedules = new TrainSchedulesFromJson(stationInfo);

        // Station identifiers for API only. Train routes use differing identifiers.
        final String firstRouteStation = stationInfo.getStation1();
        final String lastRouteStation = stationInfo.getStation2();

        // Get information from API on trains running between station1 and station2. Both ways. Combine results.
        ArrayList<TrainInformation> trains = trainSchedules.run(firstRouteStation, lastRouteStation);
        ArrayList<TrainInformation> trainsOtherWayAround = trainSchedules.run(lastRouteStation, firstRouteStation);
        trains.addAll(trainsOtherWayAround);

        // System.out.println("Number of valid trains: " + trains.size());

/*         for (TrainInformation train : trains) {
            System.out.print("Train name: ");
            System.out.println(train.getTrainName());
            for (TimeTableRow timeTableRow : train.timeTableRows) {
                System.out.print(timeTableRow.getStationShortCode());
                System.out.print(": ");
                System.out.print(timeTableRow.getScheduledTime());
                System.out.println(" (" + timeTableRow.getStopType() + ")");
                // System.out.println(" (" + timeTableRow.stopType + ")");
            }
            System.out.println("======================");
        }
*/


        // Get lower and upper bounds from all timetables. 
        // Add/substract some minutes from each to make nicer graph later.
        // NOTE: Seems this could be done with the API too.
        LocalDateTime firstDateTime = Timestamps.getEarliest(trains).minusMinutes(2);
        LocalDateTime lastDateTime = Timestamps.getLatest(trains).plusMinutes(2);
/*
        System.out.print("First DateTime: ");
        System.out.println(firstDateTime);
        System.out.print("Last DateTime: ");
        System.out.println(lastDateTime);
        System.out.println("======================");
*/
        DrawTrainGraph window = new DrawTrainGraph(firstDateTime, lastDateTime, stationInfo);
        window.draw(trains);

/*         for (int i = 0; i < stationInfo.size; i++) {
            System.out.println(stationInfo.getRelativePosition(i));
        }
         */
        System.out.println("Finished");
    }
}
