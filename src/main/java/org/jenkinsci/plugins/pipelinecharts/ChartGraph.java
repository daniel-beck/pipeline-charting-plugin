package org.jenkinsci.plugins.pipelinecharts;

import hudson.model.Job;
import hudson.model.Run;
import hudson.util.Graph;
import hudson.util.RunList;
import hudson.util.ShiftedCategoryAxis;
import org.jenkinsci.plugins.pipelinecharts.chartdata.Chart;
import org.jenkinsci.plugins.pipelinecharts.chartdata.Point;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChartGraph extends Graph {

    private Job job;
    private String chartName;

    public ChartGraph(Job job, String chartName) {
        super(Calendar.getInstance(), 640, 480);
        this.job = job;
        this.chartName = chartName;
    }

    private DefaultCategoryDataset createDataset() {
        List<RunChartAction> actions = new ArrayList<RunChartAction>();
        RunList<?> builds = job.getBuilds();
        for (Run<?, ?> run : builds) {
            RunChartAction action = run.getAction(RunChartAction.class);
            if (action == null) {
                continue;
            }
            actions.add(action);
        }

        Collections.reverse(actions);
        Set<String> statusSet = new HashSet<String>();

        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        for (RunChartAction action : actions) {
            Chart chart = action.getChart(this.chartName);
            if (chart != null) {
                for (Point point : chart.getPoints()) {
                    ds.addValue((Number) point.getY(), point.getSeries(), point.getX());
                }
            }
        }
        return ds;
    }

    @Override
    protected JFreeChart createGraph() {
        DefaultCategoryDataset dataset = createDataset();

        JFreeChart chart = ChartFactory.createStackedAreaChart(this.chartName,
                "", "", dataset, PlotOrientation.VERTICAL, true,
                true, false);
        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.black);

        CategoryAxis domainAxis = new ShiftedCategoryAxis(null);
        plot.setDomainAxis(domainAxis);
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        domainAxis.setCategoryMargin(0.0);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRange(true);

        plot.setInsets(new RectangleInsets(0, 0, 0, 5.0));
        return chart;
    }
}
