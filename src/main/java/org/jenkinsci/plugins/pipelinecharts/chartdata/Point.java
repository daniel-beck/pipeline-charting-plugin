package org.jenkinsci.plugins.pipelinecharts.chartdata;

public class Point {

    private String series;
    private double x;
    private double y;

    public Point(String series, double x, double y) {
        this.series = series;
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getSeries() {
        return series;
    }
}
