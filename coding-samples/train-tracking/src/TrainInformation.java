import java.awt.Color;
import java.util.ArrayList;

// Class for storing needed train information.
public class TrainInformation {
    public ArrayList<TimeTableRow> timeTableRows;
    private String trainNumber;
    public Color trainColor;

    public String getTrainNumber() {
        return this.trainNumber;
    }

    public void setTrainNumber(String newName) {
        this.trainNumber = newName;
    }    

    public void setTimeTableRows(ArrayList<TimeTableRow> timeTableRows) {
        this.timeTableRows = timeTableRows;
    }

    TrainInformation(String newName) {
        trainNumber = newName;
        timeTableRows = new ArrayList<TimeTableRow>();
    }

    TrainInformation() {
        trainNumber = "";
        timeTableRows = new ArrayList<TimeTableRow>();
    }

    public void addToTimeTable(TimeTableRow newRow) {
        timeTableRows.add(newRow);
    }
    

}
