package gui;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import train.*;
import java.time.LocalDateTime;

/* 
 * Class for drawing the application window. Draws everything except the train graph itself.
 */
public class DrawTrainWindow {
    private final int WINDOWWIDTH;
    private final int WINDOWHEIGTH;
    private final int GRAPHWIDTH;
    private final int GRAPHHEIGHT;
    private final int PANELGAP;
    private final int PANELWIDTH;
    private LocalDateTime firstDateTime;
    private LocalDateTime lastDateTime;
    private StationInformation stationInfo;

    /**
     * Constructor.
     * 
     * @param LocalDateTime      firstDateTime: Leftward time boundary for train
     *                           graph (x-axis of graph).
     * @param LocalDateTime      lastDateTime: Rightward time boundary for train
     *                           graph (x-axis of graph).
     * @param StationInformation stationInfo: Information on train station and their
     *                           relative distances (y-axis of graph).
     * @return void
     */
    public DrawTrainWindow(LocalDateTime firstDateTime, LocalDateTime lastDateTime, StationInformation stationInfo) {
        this.WINDOWWIDTH = 1700;
        this.WINDOWHEIGTH = 1000;
        this.GRAPHWIDTH = 1400;
        this.GRAPHHEIGHT = 900;
        this.PANELGAP = 50;
        this.PANELWIDTH = 90;
        this.firstDateTime = firstDateTime;
        this.lastDateTime = lastDateTime;
        this.stationInfo = stationInfo;
    }

    /**
     * Main method for class. Does everything, except drawing the train graph itself
     * which is done by TrainGraphPanel class.
     * 
     * @param ArrayList<TrainInformation> trains: Information on train schedules.
     * @return void
     */
    public void draw(ArrayList<TrainInformation> trains) {
        // TODO: Add a button to refresh view? 
        // Either to get new train situation of bad random colors drawing trains.

        // Left edge of the graph, expressed in minutes from day start
        int zeroPoint = (60 * this.firstDateTime.getHour()) + this.firstDateTime.getMinute();

        // Right edge of the graph, expressed in minutes from day start
        int endPoint = (60 * this.lastDateTime.getHour()) + this.lastDateTime.getMinute();

        JFrame windowFrame = new JFrame();
        // NOTE: EXIT_ON_CLOSE might break some application deployments.
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.setSize(WINDOWWIDTH, WINDOWHEIGTH);
        windowFrame.setResizable(false);
        windowFrame.setBackground(Color.LIGHT_GRAY);
        windowFrame.setLayout(null);
        final String title = "Trains between " + stationInfo.getStationName(0) + " and "
                + stationInfo.getStationName(stationInfo.size() - 1);
        windowFrame.setTitle(title);

        // Panel for text at the top.
        JPanel topPanel = new JPanel();
        topPanel.setBounds(100, 0, GRAPHWIDTH, PANELGAP);
        topPanel.setBackground(Color.LIGHT_GRAY);
        windowFrame.add(topPanel);

        // Panel for the top left corner. Mainly aesthetics.
        JPanel topLeftCornerPanel = new JPanel();
        topLeftCornerPanel.setBounds(10, 0, PANELWIDTH, PANELGAP);
        topLeftCornerPanel.setBackground(Color.LIGHT_GRAY);
        windowFrame.add(topLeftCornerPanel);

        // Panel for text on the left.
        LeftPanel leftPanel = new LeftPanel(stationInfo, this.GRAPHHEIGHT, this.PANELWIDTH);

        // Panel for the top right corner. Mainly aesthetics.
        JPanel topRightCornerPanel = new JPanel();
        topRightCornerPanel.setBounds((100 + GRAPHWIDTH), 0, PANELWIDTH, PANELGAP);
        topRightCornerPanel.setBackground(Color.LIGHT_GRAY);
        windowFrame.add(topRightCornerPanel);

        // Panel for the right. Currently for aesthetics.
        JPanel rightPanel = new JPanel();
        rightPanel.setBounds((100 + GRAPHWIDTH), 50, PANELWIDTH, GRAPHHEIGHT);
        rightPanel.setBackground(Color.LIGHT_GRAY);
        windowFrame.add(rightPanel);

        TrainGraphPanel graphPanel = new TrainGraphPanel(windowFrame, trains, zeroPoint, endPoint, this.stationInfo,
                GRAPHWIDTH, GRAPHHEIGHT);
        graphPanel.setBounds(100, 50, GRAPHWIDTH, GRAPHHEIGHT);
        graphPanel.setBackground(Color.WHITE);
        graphPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        windowFrame.add(graphPanel);

        windowFrame.add(leftPanel);
        windowFrame.setVisible(true);
    }
}
