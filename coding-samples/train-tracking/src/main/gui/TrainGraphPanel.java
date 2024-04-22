package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import train.*;
import train.StationInformation;
import train.TimeTableRow;

/* 
 * Class for drawing the main train graph of program. Other parts of GUI are handled by class DrawTrainWindow.
 */
public class TrainGraphPanel extends JPanel {
    private int width;
    private int heigth;
    private ArrayList<TrainInformation> trains;
    private int zeroPoint;
    private int endPoint;
    private StationInformation stationInfo;

    public TrainGraphPanel(JFrame windowFrame, ArrayList<TrainInformation> trains, int zeroPoint, int endPoint,
            StationInformation stationInfo, int width, int heigth) {
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
     * Method automatically called by Java. Does main work for class TrainGraphPanel.
     * @param Grahics g: Object handle given by Java for drawing on the object.
     * @return void
     */
    public void paint(Graphics g) {
        super.paint(g);

        // TODO: Add small vertical lines at y-position of stations?
        // TODO: Actual times currently combined with scheduled times.

        Graphics2D g2d = (Graphics2D) g;

        // Draw current time as a thick green vertical line.
        LocalDateTime timeNow = LocalDateTime.now();
        int nowSeconds = 60 * timeNow.getHour() + timeNow.getMinute();
        int xNow = (int) ((((float) nowSeconds - (float) zeroPoint) / ((float) endPoint - (float) zeroPoint))
                * 1400.0f);
        g2d.setStroke(new BasicStroke(5));
        g2d.setPaint(new Color(0, 255, 0));
        g2d.drawLine(xNow, 0, xNow, this.heigth);

        g2d.setStroke(new BasicStroke(2));

        int trainIndex = 0;
        int outBounds = 0;
        // Go though all scheduled trains and draw train graphs.
        for (TrainInformation train : trains) {
            g2d.setPaint(train.getTrainColor());
            g2d.drawString(train.getTrainNumber(), 5, 15 * (trainIndex + 1));

            int prevX = -1;
            int prevY = -1;

            for (TimeTableRow timeTableRow : train.getTimeTableRows()) {

                int xCoordinate = 0;
                int yCoordinate = 0;

                int totalMinutesToUse;

                if (timeTableRow.getActualTime() != null) {
                    totalMinutesToUse = 60 * timeTableRow.getActualTime().getHour()
                            + timeTableRow.getActualTime().getMinute();
                } else {
                    totalMinutesToUse = 60 * timeTableRow.getScheduledTime().getHour()
                            + timeTableRow.getScheduledTime().getMinute();
                }

                xCoordinate = (int) ((((float) totalMinutesToUse - (float) zeroPoint)
                        / ((float) endPoint - (float) zeroPoint)) * 1400.0f);

                if (xCoordinate > 1400) {
                    outBounds++;
                }
                if (xCoordinate < 0) {
                    outBounds++;
                }

                // Index (and relative y-position) for the station currently being processed.
                int stationIndex = stationInfo.getIndexForStationId(timeTableRow.getStationShortCode().toUpperCase());
                yCoordinate = (int) (900 - stationInfo.getRelativePosition(stationIndex));

                // No line to draw on the first row of each time table (two points needed for a
                // line).
                if (prevX == -1 || prevY == -1) {
                    prevX = xCoordinate;
                    prevY = yCoordinate;
                } else {
                    // Use a broader brush if drawaing an actual timetable, instead of a scheduled
                    // timetable.
                    if (timeTableRow.getActualTime() != null) {
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

        // No trains should be outside to bound of the graph. Otherwise there's a
        // critical bug in implementation.
        if (outBounds != 0) {
            System.out.println("Trains of out bounds " + (outBounds / trains.size() + " . Exiting."));
            System.exit(60);
        }
    }
}
