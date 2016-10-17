/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wildpark.wpeco.support;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

public class DayViewModel {

    private Calendar calendar = Calendar.getInstance();
    private String modelName;
    private List<ChartModel> models;
    private CartesianChartModel temperature;
    private BarChartModel fuelConsumition;
    private LineChartSeries tempInsideSeries;
    private BarChartSeries fuelConsumitionSeries;

    public DayViewModel(String modelName, double outsideTemperature2) {
        models = new ArrayList<>();
        this.modelName = modelName;
        temperature = new BarChartModel();
        temperature.setAnimate(true);
        temperature.setTitle("Температура");
        Axis y2Axis = temperature.getAxis(AxisType.X);
        y2Axis.setMin(0);
        y2Axis.setMax(24);
        fuelConsumitionSeries = new BarChartSeries();
        tempInsideSeries=new LineChartSeries("Температура в помещении");
        fuelConsumitionSeries.setLabel("Расход топлива");
        temperature.addSeries(fuelConsumitionSeries);
        temperature.addSeries(tempInsideSeries);
        temperature.setLegendPosition("nw");
        temperature.setMouseoverHighlight(false);
        temperature.setShowDatatip(false);
        temperature.setShowPointLabels(false);
    }
    
    public void addValue(double fuel,double tempInside,int date){
        temperature.getSeries().get(0).set(date, fuel);
        temperature.getSeries().get(1).set(date, tempInside);        
           
    }
    
    public String getType(ChartModel cm){
        if(cm.getTitle().contains("Температура"))return "line";
        else return "bar";
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

    public List<ChartModel> getModels() {
        return models;
    }

    public void setModels(List<ChartModel> models) {
        this.models = models;
    }

    public CartesianChartModel getTemperature() {
        return temperature;
    }

    public void setTemperature(LineChartModel temperature) {
        this.temperature = temperature;
    }

    public BarChartModel getFuelConsumition() {
        return fuelConsumition;
    }

    public void setFuelConsumition(BarChartModel fuelConsumition) {
        this.fuelConsumition = fuelConsumition;
    }

    public LineChartSeries getTempInsideSeries() {
        return tempInsideSeries;
    }

    public void setTempInsideSeries(LineChartSeries tempInsideSeries) {
        this.tempInsideSeries = tempInsideSeries;
    }

    public BarChartSeries getFuelConsumitionSeries() {
        return fuelConsumitionSeries;
    }

    public void setFuelConsumitionSeries(BarChartSeries fuelConsumitionSeries) {
        this.fuelConsumitionSeries = fuelConsumitionSeries;
    }
    
    
    

}
