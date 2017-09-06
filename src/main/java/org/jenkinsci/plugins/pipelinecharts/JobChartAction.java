package org.jenkinsci.plugins.pipelinecharts;

import hudson.model.Action;
import hudson.model.Job;
import hudson.model.Run;
import hudson.util.Graph;
import hudson.util.RunList;
import org.jenkinsci.plugins.pipelinecharts.chartdata.Chart;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JobChartAction implements Action {

    private transient Job job;

    public JobChartAction(Job job) {
        this.job = job;
    }

    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "Pipeline Charts"; // TODO i18n
    }

    @Override
    public String getUrlName() {
        return "pipeline-charts";
    }

    public Graph getDynamic(String chart) {
        return new ChartGraph(job, chart);
    }

    public Set<String> getChartNames() {
        Set<String> chartNames = new HashSet<>();
        RunList<?> builds = job.getBuilds();
        for (Run<?, ?> run : builds.limit(10)) {
            RunChartAction action = run.getAction(RunChartAction.class);
            if (action == null) {
                continue;
            }
            chartNames.addAll(action.getChartData().stream().map(Chart::getTitle).collect(Collectors.toSet()));
        }
        return chartNames;
    }
}
