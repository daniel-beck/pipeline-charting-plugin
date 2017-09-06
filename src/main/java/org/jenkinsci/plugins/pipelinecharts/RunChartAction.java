package org.jenkinsci.plugins.pipelinecharts;

import hudson.model.Action;
import hudson.model.InvisibleAction;
import hudson.model.Run;
import jenkins.model.RunAction2;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.plugins.pipelinecharts.chartdata.Chart;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RunChartAction extends InvisibleAction implements RunAction2, SimpleBuildStep.LastBuildAction {

    private transient Run run;

    private Set<Chart> chartData = new HashSet<>();

    @Override
    public Collection<? extends Action> getProjectActions() {
        return Collections.singleton(new JobChartAction(run.getParent()));
    }

    @Override
    public void onAttached(Run<?, ?> r) {
        this.run = r;
    }

    @Override
    public void onLoad(Run<?, ?> r) {
        this.run = r;
    }

    public Set<Chart> getChartData() {
        return chartData;
    }

    public Chart getChart(String chart) {
        for (Chart candidate : chartData) {
            if (candidate.getTitle().equals(chart)) {
                return candidate;
            }
        }
        return null;
    }
}
