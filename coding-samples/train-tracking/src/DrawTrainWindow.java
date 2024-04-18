import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
    private LocalDateTime firstDateTime;
    private LocalDateTime lastDateTime;
    private StationInformation stationInfo;

    /** 
     * Constructor.
     * @param LocalDateTime firstDateTime: Leftward time boundary for train graph (x-axis of graph).
     * @param LocalDateTime lastDateTime: Rightward time boundary for train graph (x-axis of graph).
     * @param StationInformation stationInfo: Information on train station and their relative distances (y-axis of graph).
     * @return void
     */
    public DrawTrainWindow(LocalDateTime firstDateTime, LocalDateTime lastDateTime, StationInformation stationInfo) {
        // TODO: Move all constansts to a single class, which is passed on to needing classes.
        this.WINDOWWIDTH = 1700;
        this.WINDOWHEIGTH = 1000;
        this.GRAPHWIDTH = 1400;
        this.GRAPHHEIGHT = 900;
        this.PANELGAP = 50;
        this.firstDateTime = firstDateTime;
        this.lastDateTime = lastDateTime;
        this.stationInfo = stationInfo;
    }
    
    /** 
     * Main method for class. Does everything, except drawing the train graph itself which is done by TrainGraphPanel class.
     * @param ArrayList<TrainInformation> trains: Information on train schedules.
     * @return void
     */
    public void draw(ArrayList<TrainInformation> trains) {
        Random rand = new Random();

        // TODO: Add a button to refresh view. Either for new train situation of bad random colors drawing trains.

        // Left edge of the graph, expressed in minutes from day start
        int zeroPoint = (60 * this.firstDateTime.getHour()) + this.firstDateTime.getMinute();

        // Right edge of the graph, expressed in minutes from day start
        int endPoint = (60 * this.lastDateTime.getHour()) + this.lastDateTime.getMinute();

        JFrame windowFrame = new JFrame();
        // NOTE: EXIT_ON_CLOSE might break some application deployments.
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.setSize(WINDOWWIDTH,WINDOWHEIGTH);
        windowFrame.setResizable(false);
        windowFrame.setBackground(Color.LIGHT_GRAY);
        windowFrame.setLayout(null);
        windowFrame.setTitle("Trains between " + stationInfo.getStationName(0) + " and " + stationInfo.getStationName(stationInfo.size()-1));

        // Panel for text at the top.
        JPanel topPanel = new JPanel();
        topPanel.setBounds(100, 0, GRAPHWIDTH, PANELGAP);
        topPanel.setBackground(Color.LIGHT_GRAY);
        windowFrame.add(topPanel);

        // Panel for the top left corner. Mainly aesthetics.
        JPanel cornerPanel = new JPanel();
        cornerPanel.setBounds(10, 0, 90, PANELGAP);
        cornerPanel.setBackground(Color.LIGHT_GRAY);
        windowFrame.add(cornerPanel);

        // Panel for text on the left.
        JPanel leftPanel = new JPanel();
        leftPanel.setBounds(10, 50, 90, GRAPHHEIGHT);
        leftPanel.setBackground(Color.LIGHT_GRAY);

        // Attempt at using several left JPanels
        ArrayList<JPanel> leftPanels = new ArrayList<JPanel>();
        int previousY = 50;
        for (int i = 0; i < stationInfo.size(); i++) {
            leftPanels.add(new JPanel());
            JPanel currentPanel = leftPanels.get(i);

            
            int newY;
            newY = (int)stationInfo.getRelativePositionLabels(i);

            // currentPanel.setLayout(null);
            currentPanel.setBounds(10, (newY+50), 90, 100);
            currentPanel.setBackground(Color.LIGHT_GRAY);
            
        
            // Add random color to panel to make it easier to distinguish. 
            // NOTE: Enable for debugging only. 
            // currentPanel.setBackground(new Color((int)(255 * rand.nextFloat()), (int)(255 * rand.nextFloat()), (int)(255 * rand.nextFloat())));
                        
            JLabel newLabel = new JLabel();
            newLabel.setFont(new Font("Serif", Font.PLAIN, 10));
            newLabel.setText(stationInfo.getStationName(i));
            // newLabel.setLayout(null);

            // TODO: label positioning not working as expected. E.g. Helsinki Asema should be at the very bottom.
            if (i == 0) {
                
                newLabel.setVerticalAlignment(JLabel.TOP);
                newLabel.setVerticalTextPosition(JLabel.TOP);
            } else {
                newLabel.setVerticalAlignment(JLabel.BOTTOM);
                newLabel.setVerticalTextPosition(JLabel.BOTTOM);
            }
            currentPanel.add(newLabel);

            previousY = previousY + newY;
            
            windowFrame.add(currentPanel);
        }
        // End of left JPanel

        TrainGraphPanel graphPanel = new TrainGraphPanel(windowFrame, trains, zeroPoint, endPoint, this.stationInfo, GRAPHWIDTH, GRAPHHEIGHT);
        graphPanel.setBounds(100, 50, GRAPHWIDTH, GRAPHHEIGHT);
        graphPanel.setBackground(Color.WHITE);
        graphPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        windowFrame.add(graphPanel);

        windowFrame.add(leftPanel);
        windowFrame.setVisible(true);
    }
}
