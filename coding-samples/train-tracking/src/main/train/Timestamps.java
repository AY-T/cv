package train;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/* 
 * Class for finding the earliest and latest timestamp for a group of TrainInformation time tables.
 */
public class Timestamps {

    /**
     * Returns earliest timestarmp for given train schedules.
     * 
     * @param ArrayList<TrainInformation> trains: List of trains and train
     *                                    schedules.
     * @return LocalDateTime: earliest found timestamp on in all train schedules.
     */
    public static LocalDateTime getEarliest(ArrayList<TrainInformation> trains) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        // Earliest time table entry, for drawing a tight train graph.
        LocalDateTime firstDateTime = LocalDateTime.parse("3000-01-01T00:00:00.001Z", formatter);

        for (TrainInformation train : trains) {
            for (TimeTableRow timeTableRow : train.getTimeTableRows()) {
                if (timeTableRow.getScheduledTime().isBefore(firstDateTime)) {
                    firstDateTime = timeTableRow.getScheduledTime();
                }
            }
        }

        // If no valid train timetables have been found, consider a critical error and
        // end the program.
        if (firstDateTime.equals(LocalDateTime.parse("3000-01-01T00:00:00.001Z", formatter))) {
            System.out.println("No valid timetables found. Exiting.");
            System.exit(30);
        }

        return firstDateTime;
    }

    /**
     * Returns latest (last) timestarmp for given train schedules.
     * 
     * @param ArrayList<TrainInformation> trains: List of trains and train
     *                                    schedules.
     * @return LocalDateTime: latest (last) found timestamp on in all train
     *         schedules.
     */
    public static LocalDateTime getLatest(ArrayList<TrainInformation> trains) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        // Latest time table entry, for drawing a tight train graph.
        LocalDateTime lastDateTime = LocalDateTime.parse("1900-01-01T00:00:00.001Z", formatter);

        for (TrainInformation train : trains) {
            for (TimeTableRow timeTableRow : train.getTimeTableRows()) {
                if (timeTableRow.getScheduledTime().isAfter(lastDateTime)) {
                    lastDateTime = timeTableRow.getScheduledTime();
                }
            }
        }

        // If no valid train timetables have been found, consider a critical error and
        // end the program.
        if (lastDateTime.equals(LocalDateTime.parse("1900-01-01T00:00:00.001Z", formatter))) {
            System.out.println("No valid timetables found. Exiting.");
            System.exit(30);
        }

        return lastDateTime;
    }
}
