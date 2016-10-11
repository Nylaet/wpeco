/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wildpark.wpeco.support;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.ChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author Panker-RDP
 */
public class MonthViewModel {

    private Calendar calendar = Calendar.getInstance();
    private String modelName;
    private double tempStart = Double.MIN_VALUE;
    private double tempEnd = Double.MAX_VALUE;
    private double summFuelConsumition = 0;
    private LineChartModel temperature;
    private LineChartSeries tempInsideSeries;
    private LineChartSeries tempOutsideSeries;
    private LineChartSeries fuelConsumitionSeries;

    public MonthViewModel(String modelName, double tempStart) {
        this.modelName = modelName;
        this.tempStart = tempStart;
        temperature = new LineChartModel();
        temperature.setAnimate(true);
        temperature.setTitle("Температура");
        Axis y2Axis = temperature.getAxis(AxisType.X);
        y2Axis.setLabel("Дни");
        y2Axis.setMin(1);
        y2Axis.setMax(30);
        tempInsideSeries = new LineChartSeries("Температура в помещении");
        tempOutsideSeries = new LineChartSeries("Температура снаружи");
        fuelConsumitionSeries = new LineChartSeries();
        fuelConsumitionSeries.setLabel("Расход топлива кг/час");
        temperature.addSeries(tempInsideSeries);
        temperature.addSeries(tempOutsideSeries);
        temperature.addSeries(fuelConsumitionSeries);
        temperature.setLegendPosition("se");
    }

    public void addValue(double fuel, double tempInside, double tempOutside, Calendar date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String txt = sdf.format(date.getTime());
        temperature.getSeries().get(0).set(txt, tempInside);
        temperature.getSeries().get(1).set(txt, tempOutside);
        temperature.getSeries().get(2).set(txt, fuel);
        if (tempOutside > tempStart) {
            tempStart = tempOutside;
        }
        if (tempOutside < tempEnd) {
            tempEnd = tempOutside;
        }
        summFuelConsumition += fuel;

    }

    public String getType(ChartModel cm) {
        if (cm.getTitle().contains("Температура")) {
            return "line";
        } else {
            return "bar";
        }
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public double getTempStart() {
        return tempStart;
    }

    public void setTempStart(double tempStart) {
        this.tempStart = tempStart;
    }

    public double getTempEnd() {
        return tempEnd;
    }

    public void setTempEnd(double tempEnd) {
        this.tempEnd = tempEnd;
    }

    public double getSummFuelConsumition() {
        return summFuelConsumition;
    }

    public void setSummFuelConsumition(double summFuelConsumition) {
        this.summFuelConsumition = summFuelConsumition;
    }

    public LineChartModel getTemperature() {
        return temperature;
    }

    public void setTemperature(LineChartModel temperature) {
        this.temperature = temperature;
    }

    public LineChartSeries getTempInsideSeries() {
        return tempInsideSeries;
    }

    public void setTempInsideSeries(LineChartSeries tempInsideSeries) {
        this.tempInsideSeries = tempInsideSeries;
    }

    public LineChartSeries getTempOutsideSeries() {
        return tempOutsideSeries;
    }

    public void setTempOutsideSeries(LineChartSeries tempOutsideSeries) {
        this.tempOutsideSeries = tempOutsideSeries;
    }

}
