package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import train.StationInformation;

/* 
 * Class for drawing panel left of the train graph. Includes labels for stations.
 */
public class LeftPanel extends JPanel {
    private StationInformation stationInfo;

    public LeftPanel(StationInformation stationInfo) {
        this.setBounds(10, 50, 90, 900);
        this.setBackground(Color.LIGHT_GRAY);
        this.stationInfo = stationInfo;
    }


    /**
     * Method automatically called by Java. Does main work for class LeftPanel.
     * @param Grahics g: Object handle given by Java for drawing on the object.
     * @return void
     */    
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        final int FONTSIZE = 12;
        g2d.setFont(new Font("Serif", Font.PLAIN, FONTSIZE));
        g2d.setColor(Color.BLACK);

        // Draw station labels on the left panel.
        for (int i = 0; i < stationInfo.size(); i++) {
            int yPosition = (int) (900 - stationInfo.getRelativePositionLabels(i));
            String stationName = stationInfo.getStationName(i);

            if (i == stationInfo.size() - 1) {
                g2d.drawString(stationName, 5, (900 - FONTSIZE));
            } else if (i == 0) {
                g2d.drawString(stationName, 5, (yPosition + FONTSIZE));
            } else if (i >= 1 && i < stationInfo.size()) {
                g2d.drawString(stationName, 5, (yPosition + (FONTSIZE / 2)));
            }
        }
    }
}
