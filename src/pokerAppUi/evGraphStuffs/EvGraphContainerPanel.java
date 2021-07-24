package pokerAppUi.evGraphStuffs;

import static constants.SixPlusConstants.NUM_PREFLOP_HANDS;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.*;
import org.jfree.ui.RectangleEdge;

import solvers.ev_graph.EvGraphResults;
import utils.ArrayUtils;
import utils.StringFormatting;

@SuppressWarnings("serial")
public class EvGraphContainerPanel
        extends JPanel
        implements ChartMouseListener {

    private XYSeriesCollection dataset;

    private EvGraphPanel chartPanel;

    private Crosshair xCrosshair;

    private Crosshair yCrosshair;

    private static final double EPSILON = 0.01;

    double[][] noData = { { 0, 0 } };

    public EvGraphContainerPanel() {
        this.setMinimumSize(new Dimension(300, 400));
        this.setMaximumSize(new Dimension(1000, 400));
        // dataset = createGraphData(noData, 0);
        // createChartPanel(0.0, 0);
        // add(chartPanel);
        // validate();
    }

    public void resetGraph() {
        updateGraph(noData, Double.NaN, 0);
    }

    public void updateGraph(EvGraphResults graphResults) {
        double[][] cumulativeFractionsAndEv = graphResults.getCumulativeFractionsAndEv();
        double nashFraction = graphResults.getNashFraction();
        double nashEv = graphResults.getNashEv();
        // updateGraph(cumulativeFractionsAndEv, nashFraction, nashEv);

        if (chartPanel != null) {
            remove(chartPanel);
        }
        dataset = createGraphData(cumulativeFractionsAndEv, nashFraction);
        createChartPanel(nashFraction, 
                         nashEv, 
                         graphResults.getHandString(),
                         graphResults.getxAxisTitle(),
                         graphResults.getyAxisTitle());
        add(chartPanel, BorderLayout.CENTER);

        validate();
    }

    private void updateGraph(double[][] cumulativeFractionsAndEv, double nashFraction, double nashEv) {
        // long start = System.currentTimeMillis();
        remove(chartPanel);
        dataset = createGraphData(cumulativeFractionsAndEv, nashFraction);
        createChartPanel(nashFraction, nashEv);
        add(chartPanel, BorderLayout.CENTER);
        // if (100 * nashFraction < EPSILON) {
        // TextTitle textTitle = new TextTitle("Nash range 0 %, graph may be unreliable");
        // textTitle.setPaint(Color.ORANGE);
        // chartPanel.getChart().setTitle(textTitle);
        // }
        validate();
    }

    private double[] getEvs(double[][] cumulativeFractionsAndEv) {
        double[] eVs = new double[cumulativeFractionsAndEv.length];
        for (int i = 0; i < eVs.length; i++) {
            eVs[i] = cumulativeFractionsAndEv[i][1];
        }
        return eVs;
    }

    public final XYSeriesCollection createGraphData(double[][] cumulativeFractionsAndEv,
                                                    double nashFraction) {
        XYSeriesCollection graphAndNashLine = new XYSeriesCollection();
        XYSeries graphData = new XYSeries("EV");
        for (int i = 0; i < cumulativeFractionsAndEv.length; i++) {
            double[] data = cumulativeFractionsAndEv[i];
            graphData.add(100.0 * data[0], data[1]);
        }
        graphAndNashLine.addSeries(graphData);

        XYSeries nashLine = makeNashLine(cumulativeFractionsAndEv, nashFraction);
        graphAndNashLine.addSeries(nashLine);
        return graphAndNashLine;
    }

    private XYSeries makeNashLine(double[][] cumulativeFractionsAndEv, double nashFraction) {
        XYSeries nashLine = new XYSeries("Nash");
        double[] eVs = getEvs(cumulativeFractionsAndEv);
        double evMin = Math.min(0, ArrayUtils.min(eVs));
        double evMax = Math.max(0, ArrayUtils.max(eVs));
        double step = (evMax - evMin) / 100.0;
        for (int i = 0; i < 100; i++) {
            nashLine.add(100 * nashFraction, evMin + i * step);
        }
        return nashLine;
    }
    
    private void createChartPanel(double nashFraction, double nashEv) {
        createChartPanel(nashFraction, nashEv);
    }

    private void createChartPanel(double nashFraction,
                                  double nashEv,
                                  String title,
                                  String xAxisLabel,
                                  String yAxisLabel) {
        JFreeChart chart = createChart(dataset, title, xAxisLabel, yAxisLabel);
        this.chartPanel = new EvGraphPanel(chart);
        changeLookAndFeel(chart, nashFraction, nashEv);
    }

    // TODO Review
    private BreakEvenPointInfo findBreakEvenPoint() {
        double breakEvenX = 0;
        double minDiff = Double.POSITIVE_INFINITY;
        int beNumHandsPlayed = NUM_PREFLOP_HANDS;
        boolean foundPositive = false;
        boolean foundNegative = false;

        for (int i = 0; i < dataset.getItemCount(0); i++) {
            double x = dataset.getXValue(0, i);
            double y = DatasetUtilities.findYValue(dataset, 0, x);
            foundPositive |= y > 0;
            foundNegative |= y < 0;

            if (Math.abs(y) < minDiff) {
                breakEvenX = x;
                minDiff = Math.abs(y);
                beNumHandsPlayed = i;
            }
        }
        // if (beNumHandsPlayed <= 0 && minDiff > EPSILON) {
        // breakEvenX = 0.0;
        // }
        if (foundPositive && !foundNegative) {
            breakEvenX = 0.0;
        }
        if (!foundPositive && foundNegative) {
            breakEvenX = 0;
            beNumHandsPlayed = NUM_PREFLOP_HANDS - 1;
        }
        return new BreakEvenPointInfo(breakEvenX, beNumHandsPlayed);
    }

    private void changeLookAndFeel(JFreeChart chart, double nashFraction, double nashEv) {
        this.chartPanel.addChartMouseListener(this);
        CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
        this.xCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
        this.xCrosshair.setLabelVisible(true);
        this.yCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
        this.yCrosshair.setLabelVisible(true);
        crosshairOverlay.addDomainCrosshair(xCrosshair);
        crosshairOverlay.addRangeCrosshair(yCrosshair);
        chartPanel.addOverlay(crosshairOverlay);

        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(false);

        XYPlot plot = chart.getXYPlot();

        makeXaxisBolder(plot);
        setRenderers(plot);
        setFonts(plot);
        setRange(plot);

        BreakEvenPointInfo breakEvenPoint = findBreakEvenPoint();
        setBreakEvenPointCrosshair(breakEvenPoint);

        chartPanel.updateLabels(breakEvenPoint, nashFraction);

        setChartTitle(chart, nashEv);
    }

    private void setChartTitle(JFreeChart chart, double nashEv) {
        String sign = nashEv > 0 ? "+" : "";
        String chartTitle = "Nash EV: " + sign + StringFormatting.decimalFormat(nashEv, 2);
        chart.setTitle(chartTitle);
    }

    private void setFonts(XYPlot plot) {
        Font labelFont = new Font("Dialog", Font.PLAIN, 16);
        plot.getDomainAxis().setLabelFont(labelFont);
        plot.getRangeAxis().setLabelFont(labelFont);

        Font tickFont = new Font("Dialog", Font.BOLD, 15);
        plot.getDomainAxis().setTickLabelFont(tickFont);
        plot.getRangeAxis().setTickLabelFont(tickFont);
    }

    private void setRange(XYPlot plot) {
        Range xRange = plot.getRangeAxis().getRange();
        double margin = 0.5;
        Range newXRange = new Range(Math.min(xRange.getLowerBound(), -margin),
                                    Math.max(xRange.getUpperBound(), margin));
        plot.getRangeAxis().setRange(newXRange);
    }

    private void setRenderers(XYPlot plot) {
        XYLineAndShapeRenderer graphRenderer = new XYLineAndShapeRenderer();
        graphRenderer.setSeriesPaint(0, Color.GREEN);
        graphRenderer.setSeriesStroke(0, new BasicStroke(4));
        graphRenderer.setSeriesShapesVisible(0, false);

        // XYLineAndShapeRenderer nashLineRenderer = new XYLineAndShapeRenderer();
        graphRenderer.setSeriesPaint(1, Color.MAGENTA);
        graphRenderer.setSeriesStroke(1, new BasicStroke(2));
        graphRenderer.setSeriesShapesVisible(1, false);

        plot.setRenderer(0, graphRenderer);
    }

    private void makeXaxisBolder(XYPlot plot) {
        plot.setRangeZeroBaselineVisible(true);
        plot.setRangeZeroBaselinePaint(Color.RED);
        plot.setRangeZeroBaselineStroke(new BasicStroke(4));
    }

    private void setBreakEvenPointCrosshair(BreakEvenPointInfo breakEvenPoint) {
        double beNumHandsPlayed = breakEvenPoint.getBeNumHandsPlayed();
        if (beNumHandsPlayed >= 0 && beNumHandsPlayed < NUM_PREFLOP_HANDS - 1)
            this.xCrosshair.setValue(breakEvenPoint.getBreakEvenRangeFraction());
    }

    private JFreeChart createChart(XYDataset dataset) {
        return createChart(dataset, "Ev graph", "Range %", "EV");
    }

    private JFreeChart createChart(XYDataset dataset,
                                   String title,
                                   String xAxisLabel,
                                   String yAxisLabel) {
        JFreeChart chart = ChartFactory.createXYLineChart(title, xAxisLabel, yAxisLabel, dataset);
        NumberAxis domain = (NumberAxis) chart.getXYPlot().getDomainAxis();
        domain.setRange(0, 100);
        return chart;
    }

    @Override
    public void chartMouseClicked(ChartMouseEvent event) {
        // ignore
    }

    @Override
    public void chartMouseMoved(ChartMouseEvent event) {
        Rectangle2D dataArea = this.chartPanel.getScreenDataArea();
        JFreeChart chart = event.getChart();
        XYPlot plot = (XYPlot) chart.getPlot();
        ValueAxis xAxis = plot.getDomainAxis();
        double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, RectangleEdge.BOTTOM);
        double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
        this.xCrosshair.setValue(x);
        this.yCrosshair.setValue(y);
    }

    static class BreakEvenPointInfo {
        private double breakEvenRangeFraction;
        private double beNumHandsPlayed;

        public BreakEvenPointInfo(double breakEvenRangeFraction, double beNumHandsPlayed) {
            this.breakEvenRangeFraction = breakEvenRangeFraction;
            this.beNumHandsPlayed = beNumHandsPlayed;
        }

        public double getBreakEvenRangeFraction() {
            return breakEvenRangeFraction;
        }

        public double getBeNumHandsPlayed() {
            return beNumHandsPlayed;
        }

    }

}
