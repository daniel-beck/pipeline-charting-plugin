package org.jenkinsci.plugins.pipelinecharts.chartdata;

public class Point {

    private String series;
    private String x;
    private double y;

    public Point(String series, String x, double y) {
        this.series = series;
        this.x = x;
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getSeries() {
        return series;
    }
}
