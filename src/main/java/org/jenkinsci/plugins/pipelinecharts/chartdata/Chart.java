package org.jenkinsci.plugins.pipelinecharts.chartdata;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Chart {
    private final String title;

    private Set<Point> points = new HashSet<>();

    public Chart(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void addPoint(Point point) {
        this.points.add(point);
    }

    public Set<Point> getPoints() {
        return Collections.unmodifiableSet(points);
    }
}
