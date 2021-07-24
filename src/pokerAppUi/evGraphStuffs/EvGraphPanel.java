package pokerAppUi.evGraphStuffs;

import java.awt.*;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import pokerAppUi.evGraphStuffs.EvGraphContainerPanel.BreakEvenPointInfo;
import utils.StringFormatting;
import constants.SixPlusConstants;

@SuppressWarnings("serial")
public class EvGraphPanel
        extends ChartPanel {

    private BreakEvenPointInfo breakEvenPoint = new BreakEvenPointInfo(Double.NaN, 0);
    private double nashRangePercentage = Double.NaN;

    private Dimension preferredSize = new Dimension(600, 370);

    public EvGraphPanel(JFreeChart chart) {
        super(chart);
    }

    public void updateLabels(BreakEvenPointInfo breakEvenPoint, double nashRangeFraction) {
        this.breakEvenPoint = breakEvenPoint;
        this.nashRangePercentage = 100 * nashRangeFraction;
    }

    @Override
    public Dimension getPreferredSize() {
        return preferredSize;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Dialog", Font.BOLD, 20));
        if (!Double.isNaN(nashRangePercentage)) {
            String nashLabel = "Nash: " + StringFormatting.decimalFormat(nashRangePercentage, 1, 1)
                               + "%";
            g.drawString(nashLabel, 50, getHeight() - 20);
        }

        if (!Double.isNaN(breakEvenPoint.getBreakEvenRangeFraction())) {
            boolean alwaysNegative = breakEvenPoint.getBeNumHandsPlayed() >= SixPlusConstants.NUM_PREFLOP_HANDS - 1;
            double beRangeFraction = breakEvenPoint.getBreakEvenRangeFraction();
            String breakEvenPercentage = alwaysNegative ? "N/A"
                                                       : StringFormatting.decimalFormat(beRangeFraction,
                                                                                        1, 1)
                                                         + "%";
            String nashLabel = "BE: " + breakEvenPercentage;
            g.drawString(nashLabel, getWidth() - 200, getHeight() - 20);
        }

    }
}
