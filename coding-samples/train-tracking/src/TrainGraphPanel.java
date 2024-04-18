
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;


/* 
 * Class for ...
 */
public class TrainGraphPanel extends JPanel {
    private int width;
    private int heigth;
    private ArrayList<TrainInformation> trains;
    private int zeroPoint;
    private int endPoint;
    private StationInformation stationInfo;

    public TrainGraphPanel(JFrame windowFrame, ArrayList<TrainInformation> trains, int zeroPoint, int endPoint, StationInformation stationInfo, int width, int heigth) {
        this.zeroPoint = zeroPoint;
        this.endPoint = endPoint;
        this.trains = trains;
        this.stationInfo = stationInfo;
        this.width = width;
        this.heigth = heigth;

        this.setBounds(100, 50, this.width, this.heigth);
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setVisible(true);
    }
    /** 
     * 
     * Automatically called by Java.
     * @param 
     * @return void
     */
    public void paint(Graphics g) {
        super.paint(g);

        // TODO: Change trainNumber to trainName, but cannot find train name info on given API.
        // TODO: Fix y-positioning using relative station distances. Add small vertical lines at position of stations?
        // TODO: Lines to objects to attach train number with hoves (is there need if names not available?).
        // TODO: Actual times currently combined with scheduled times. Separate to different lines? How to keep end result visually clear?

        Graphics2D g2d = (Graphics2D) g;

        // Draw current time as a thick green vertical line.
        LocalDateTime timeNow = LocalDateTime.now();
        int nowSeconds = 60 * timeNow.getHour() + timeNow.getMinute();
        int xNow = (int) ((((float)nowSeconds - (float)zeroPoint) / ((float)endPoint - (float)zeroPoint)) * 1400.0f);
        g2d.setStroke(new BasicStroke(5));
        g2d.setPaint(new Color(0,255,0));
        g2d.drawLine(xNow, 0, xNow, this.heigth);


        g2d.setStroke(new BasicStroke(2));


        int trainIndex = 0;
        int outBounds = 0;
        // Go though all scheduled trains and draw train graphs.
        for (TrainInformation train : trains) {
            g2d.setPaint(train.trainColor);
            g2d.drawString(train.getTrainNumber(), 5, 15*(trainIndex+1));


            int prevX = -1;
            int prevY = -1;

            for (TimeTableRow timeTableRow : train.timeTableRows) {

                int xCoordinate = 0;
                int yCoordinate = 0;

                int totalMinutesToUse;

                if (timeTableRow.getActualTime() != null ) {
                    totalMinutesToUse = 60 * timeTableRow.getActualTime().getHour() + timeTableRow.getActualTime().getMinute();
                } else {
                    totalMinutesToUse = 60 * timeTableRow.getScheduledTime().getHour() + timeTableRow.getScheduledTime().getMinute();
                }

                xCoordinate = (int) ((((float)totalMinutesToUse - (float)zeroPoint) / ((float)endPoint - (float)zeroPoint)) * 1400.0f);

                if (xCoordinate > 1400) {
                    outBounds++;
                }
                if (xCoordinate < 0) {
                    outBounds++;
                }
               
                // Index (and thus relative y-position) for the station currently being processed.
                // NOTE: Current implementation positions stations evenly. Changed in class StationInformation.
                int stationIndex = stationInfo.getIndexForStationId(timeTableRow.getStationShortCode().toUpperCase());
                yCoordinate = (int)  (900 - stationInfo.getRelativePosition(stationIndex));

                // No line to draw on the first row of each time table (two points needed for a line).
                if (prevX == -1 || prevY == -1 ) {
                    prevX = xCoordinate;
                    prevY = yCoordinate;                   
                } else {
                    // Use a broader brush if drawaing an actual timetable, instead of a scheduled timetable.
                    if (timeTableRow.getActualTime() != null ) {
                        g2d.setStroke(new BasicStroke(4));
                        g2d.drawLine(prevX, prevY, xCoordinate, yCoordinate);
                    } else {
                        g2d.setStroke(new BasicStroke(2));
                        g2d.drawLine(prevX, prevY, xCoordinate, yCoordinate);
                    }
                }

                prevX = xCoordinate;
                prevY = yCoordinate;
                
            } // End of timeTableRows iteration

            trainIndex++; 

        } // End of trains iteration
     
        // No trains should be outside to bound of the graph. Otherwise there's a critical bug in implementation.
        if (outBounds != 0) {
            System.out.println("Trains of out bounds " + (outBounds/trains.size() + " . Exiting."));
            System.exit(60);
        }
    }    
}
