import java.time.LocalDateTime;
import java.util.ArrayList;
import api.TrainSchedulesFromJson;
import gui.DrawTrainWindow;
import train.*;
import train.StationInformation;

/* 
 * Class for starting the train graph application.
 */
public class App {
    
    /**
     * Main method for class. Does everything. Setups everything needed and passes
     * work to DrawTrainWindow.draw().
     * 
     * @return void
     */
    public static void run() {
        // Amount of hours before and after current time to accept train timestamps for.
        final int timeSpan = 4;

        // NOTE: Used route and station hard coded into StationInformation.
        StationInformation stationInfo = new StationInformation();

        TrainSchedulesFromJson trainSchedules = new TrainSchedulesFromJson(stationInfo);

        // Station identifiers for API only. Train routes use differing identifiers.
        final String firstRouteStation = stationInfo.getStation1();
        final String lastRouteStation = stationInfo.getStation2();

        // Get information from API on trains running between station1 and station2.
        // Both ways. Combine results.
        // NOTE: Consider using List instead of ArrayList.
        ArrayList<TrainInformation> trains = trainSchedules.run(firstRouteStation, lastRouteStation, timeSpan);
        ArrayList<TrainInformation> trainsOtherWayAround = trainSchedules.run(lastRouteStation, firstRouteStation, timeSpan);
        trains.addAll(trainsOtherWayAround);

        // Add or substract some minutes from each to make nicer looking graph later.
        LocalDateTime firstDateTime = Timestamps.getEarliest(trains).minusMinutes(15);
        LocalDateTime lastDateTime = Timestamps.getLatest(trains).plusMinutes(10);

        // With all infotmation gathered, draw the train graph.
        DrawTrainWindow window = new DrawTrainWindow(firstDateTime, lastDateTime, stationInfo);
        window.draw(trains);
    }
}
