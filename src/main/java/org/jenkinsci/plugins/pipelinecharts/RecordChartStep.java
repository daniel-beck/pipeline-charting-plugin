package org.jenkinsci.plugins.pipelinecharts;

import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.pipelinecharts.chartdata.Chart;
import org.jenkinsci.plugins.pipelinecharts.chartdata.Point;
import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.jenkinsci.plugins.workflow.steps.SynchronousStepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RecordChartStep extends Step {

    private String chart = "default";
    private String series = "default";
    private Double x;
    private double y;

    @DataBoundConstructor
    public RecordChartStep(double y) {
        this.y = y;
    }

    @DataBoundSetter
    public void setChart(String chart) {
        this.chart = chart;
    }

    public String getChart() {
        return chart;
    }

    @DataBoundSetter
    public void setSeries(String series) {
        this.series = series;
    }

    public String getSeries() {
        return series;
    }

    @DataBoundSetter
    public void setX(Double x) {
        this.x = x;
    }

    public Double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new Execution(this, context);
    }

    public static class Execution extends SynchronousStepExecution<GraphInfo> {

        private transient RecordChartStep step;

        Execution(RecordChartStep step, StepContext context) {
            super(context);
            this.step = step;
        }

        @Override
        protected GraphInfo run() throws Exception {
            double x = step.getX() == null ? ((double) getContext().get(Run.class).getNumber()) : step.getX();

            RunChartAction rca = getContext().get(Run.class).getAction(RunChartAction.class);
            if (rca == null) {
                rca = new RunChartAction();
                getContext().get(Run.class).addAction(rca);
            }

            Chart chart = findChartForTitle(step.getChart(), rca);
            if (chart == null) {
                chart = new Chart(step.getChart());
            }
            chart.addPoint(new Point(step.getSeries(), x, step.getY()));

            rca.getChartData().add(chart);

            return new GraphInfo();
        }

        private static Chart findChartForTitle(String title, RunChartAction rca) {
            for (Chart chart : rca.getChartData()) {
                if (chart.getTitle().equals(title)) {
                    return chart;
                }
            }
            return null;
        }
    }

    @Extension
    public static class DescriptorImpl extends StepDescriptor {
        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return new HashSet<>(Arrays.asList(Run.class, TaskListener.class));
        }

        @Override
        public String getFunctionName() {
            return "addChartData";
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Record Chart Data";
        }
    }
}
