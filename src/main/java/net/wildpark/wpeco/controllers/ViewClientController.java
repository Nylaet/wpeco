/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wildpark.wpeco.controllers;

import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import net.wildpark.wpeco.entitys.DataHistory;
import net.wildpark.wpeco.entitys.Log;
import net.wildpark.wpeco.entitys.RemouteSensorPanel;
import net.wildpark.wpeco.entitys.SNMPClient;
import net.wildpark.wpeco.enums.LoggerLevel;
import net.wildpark.wpeco.facades.LogFacade;
import net.wildpark.wpeco.facades.RemouteSensorPanelFacade;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author Panker-RDP
 */
@Named(value = "viewClientController")
@RequestScoped
public class ViewClientController implements Serializable {

    @EJB
    RemouteSensorPanelFacade panelFacade;
    @EJB
    LogFacade logFacade;
    private List<RemouteSensorPanel> rsps=new ArrayList<>();
    
    public ViewClientController() {
    }
    
    @PostConstruct
    public void init(){
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }        
    }

    public String getLastValue(SNMPClient client){
        int index=client.getDataHistory().size()-1;
        double valueD=client.getDataHistory().get(index).getValue();
        int valueI=(int)Math.floor(valueD);
        String valueS=String.valueOf(valueI);
        return valueS;
    }
    
    public String getDateUpdate(SNMPClient client){
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yy HH:mm");
        int index=client.getDataHistory().size()-1;
        Date date=client.getDataHistory().get(index).getAddedDate();
        return sdf.format(date);
        
    }
    
    public ChartModel getClientChart(SNMPClient client){
        LineChartModel lcm=new LineChartModel();
        lcm.setAnimate(false);
        lcm.setTitle(client.getName()+": "+getLastValue(client));
        lcm.addSeries(getRemateredSeries(client.getDataHistory()));
        Axis yAxis=getMinMaxY(lcm);
        return lcm;
    }
    
    public List<RemouteSensorPanel> getRsps() {
        try{
           if(rsps.isEmpty()){
               rsps=panelFacade.findAll();
           }
        }catch(NullPointerException ex){
            logFacade.create(new Log("Error with read from base",LoggerLevel.FATAL));
        }
        return rsps;
    }

    public void setRsps(List<RemouteSensorPanel> rsps) {
        this.rsps = rsps;
    }

    private ChartSeries getRemateredSeries(List<DataHistory> dataHistory) {
        LineChartSeries lcs=new LineChartSeries();
        int median=20;
        if(dataHistory.size()>=median){
            int startIndex=dataHistory.size()-median;
            for(int i=startIndex,y=1;i<dataHistory.size();i++,y++){
                lcs.set(y,dataHistory.get(i).getValue());
            }
        }else{
            for(int i=0;i<dataHistory.size();i++){
                lcs.set(i,dataHistory.get(i).getValue());
            }
        }
        return lcs;
        
    }

    private Axis getMinMaxY(LineChartModel lcm) {
        Axis yAxis=lcm.getAxis(AxisType.Y);
        int min=Integer.MAX_VALUE;
        int max=Integer.MIN_VALUE;
        for (ChartSeries sery : lcm.getSeries()) {
            for (Map.Entry<Object, Number> entry : sery.getData().entrySet()) {
                int tempValue=(int)Math.floor((double)entry.getValue());
                if(tempValue<min)min=tempValue;
                if(tempValue>max)max=tempValue;
                
            }
        }
        yAxis.setMin(min-1);
        yAxis.setMax(max+1);
        return yAxis;
    }
    
}
