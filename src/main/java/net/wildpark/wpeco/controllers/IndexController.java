package net.wildpark.wpeco.controllers;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import net.wildpark.wpeco.support.DayViewModel;
import net.wildpark.wpeco.support.MonthViewModel;

@Named(value = "indexController")
@SessionScoped
public class IndexController implements Serializable {

    private double squareHome;
    private double heightHome;
    private final double thickness_wall_brick = 640;//Толщина стены кирпичной мм
    private final double transcalency_wall_brick = 0.7;//Коэффициент теплопроводности стены кирпичной ккал/м
    private final double thickness_wall_plaster = 15;//Толщина стены штукатурка мм
    private final double transcalency_wall_plaster = 0.6;//Коэфициент теплопроводности стены штукатурка ккал/м
    private final double transcalency_resistance_roof_floor = 0.133;//Сопротивление теплопереходу пола и потолка м.кв*час*град/ккал
    private final double transcalency_resistance_outDoor_wall = 0.005;//Сопротивление теплопереходу наружных стен м.кв*час*град/ккал
    private final double heat_capacity_air = 1.005;//Удельная теплоемкость воздуха
    private double stateOfTimeHeatLoss;//Постоянная времени обогреваемого помещения
    private double outsideTemperature = -15;//Температура забортная
    private double insideTemperature = 8;//Температура внутри
    private double needInsideTemperature = 18;//Температура внутри
    private double lenghtWall;
    private double widthWall;//Габариты помещения м.
    private double heightWall;
    private double fuelConsumition = 0;
    private double fuelConsumition2 = 0;
    private boolean outputIsReady = false;
    private boolean outputIsReady2 = false;
    private double insideTemperature2 = 5.0;
    private double outsideTemperature2 = -15.0;
    private double needTemperature2 = 18.0;
    private DayViewModel oneDay;
    private List<MonthViewModel> monthViewModels = new ArrayList<>();

    public IndexController() {
    }
    
    @PostConstruct
    public void init(){
        
    }

    public void calculateHeatSeason() {
        if (squareHome < 250) {
            setSquareHome(250.0);
        }
        if (heightHome < 3) {
            setHeightHome(3.0);
        }
        
        monthViewModels.clear();
        Calendar start = Calendar.getInstance();
        start.set(Calendar.MONTH, 10);
        start.set(Calendar.DAY_OF_MONTH, 1);
        outsideTemperature = 5.0;
        insideTemperature = 15.0;
        double plusTemp = 0.5;
        double minusTemp = -1.5;

        long day = 86400000;
        SimpleDateFormat sdf = new SimpleDateFormat("MMMMMMMM");
        while (start.get(Calendar.MONTH) != 4) {
            start.setTimeInMillis(start.getTimeInMillis() + day);
            if(start.get(Calendar.MONTH) == 4)break;
            double fuelConsumitionInDay = 0.0;
            if (start.get(Calendar.MONTH) == 10) {
                plusTemp = 1.0;
                minusTemp = -2.0;
            }
            if (start.get(Calendar.MONTH) == 11) {
                plusTemp = 1.5;
                minusTemp = -2;
            }
            if (start.get(Calendar.MONTH) == 0) {
                plusTemp = 1.5;
                minusTemp = -2;
            }
            if (start.get(Calendar.MONTH) == 1) {
                plusTemp = 1;
                minusTemp = -2;
            }
            if (start.get(Calendar.MONTH) == 2) {
                plusTemp = 2.5;
                minusTemp = -1;
            }
            if (start.get(Calendar.MONTH) == 3) {
                plusTemp = 3.0;
                minusTemp = -0.5;
            }
            if (outsideTemperature > -20 && outsideTemperature < 10) {
                outsideTemperature += (new Random().nextBoolean() ? plusTemp : minusTemp);
            } else if (outsideTemperature > 10) {
                outsideTemperature += minusTemp;
            } else {
                outsideTemperature += plusTemp;
            }
            for (int i = 0; i < 1440; i++) {
                insideTemperature += getTempAfter() / 60.0;
                if (insideTemperature > needInsideTemperature) {
                    if (fuelConsumition != 0) {
                        fuelConsumition -= 0.1;
                    }
                }
                if (insideTemperature < needInsideTemperature) {
                    if (fuelConsumition < 10) {
                        fuelConsumition += 0.1;
                    }
                }
                fuelConsumitionInDay += fuelConsumition / 1440;
            }
            if (monthViewModels.isEmpty()) {
                MonthViewModel mvm = new MonthViewModel(sdf.format(start.getTime()), outsideTemperature);
                monthViewModels.add(mvm);
            } else {
                boolean exist = false;
                for (MonthViewModel monthViewModel : monthViewModels) {
                    if (monthViewModel.getModelName().equals(sdf.format(start.getTime()))) {
                        exist = true;
                        monthViewModel.addValue(fuelConsumitionInDay, insideTemperature, outsideTemperature, start);
                    }
                }
                if (!exist) {
                    MonthViewModel mvm = new MonthViewModel(sdf.format(start.getTime()), outsideTemperature);
                    mvm.addValue(fuelConsumitionInDay, insideTemperature, outsideTemperature, start);
                    monthViewModels.add(mvm);
                }
            }

        }
        outputIsReady = true;
    }

    public void calculateOneDay() {
        outputIsReady2=true;
        oneDay=new DayViewModel("Один день из жизни котла", outsideTemperature2);
        fuelConsumition2=5.5;
        for (int i = 0, y = 0; i < 24; i++, y++) {
            insideTemperature2 += getTempAfterForOneDay();
            if (insideTemperature2 > needTemperature2) {
                if (fuelConsumition2 != 0) {
                    fuelConsumition2 -= 0.1;
                }
            }
            if (insideTemperature2 < needTemperature2) {
                if (fuelConsumition2 < 10) {
                    fuelConsumition2 += 0.1;
                }
            }
            oneDay.addValue(fuelConsumition2, insideTemperature2, i);
        }
        
    }

    public double getTempAfter() {
        double incomingHeat = 3880 * fuelConsumition;
        double heatLossResistanceWall = (thickness_wall_brick / 1000) / transcalency_wall_brick + ((thickness_wall_plaster / 1000) / transcalency_wall_plaster) + 0.133 + 0.05;
        double koefOfHeatLossResistanceHouse = 1 / heatLossResistanceWall * 4.19;
        double square = (lenghtWall * 2 + widthWall * 2) * heightWall;
        double kF = koefOfHeatLossResistanceHouse * square;

        double value = ((incomingHeat / kF) + outsideTemperature - insideTemperature) / getStateOfTimeHeatLoss();
        return value;
    }
    
    public double getTempAfterForOneDay() {
        double incomingHeat = 3880 * fuelConsumition2;
        double heatLossResistanceWall = (thickness_wall_brick / 1000) / transcalency_wall_brick + ((thickness_wall_plaster / 1000) / transcalency_wall_plaster) + 0.133 + 0.05;
        double koefOfHeatLossResistanceHouse = 1 / heatLossResistanceWall * 4.19;
        double square = (lenghtWall * 2 + widthWall * 2) * heightWall;
        double kF = koefOfHeatLossResistanceHouse * square;

        double value = ((incomingHeat / kF) + outsideTemperature2 - insideTemperature2) / getStateOfTimeHeatLoss();
        return value;
    }

    public double getStateOfTimeHeatLoss() {
        double heatLossResistanceWall = (thickness_wall_brick / 1000) / transcalency_wall_brick + ((thickness_wall_plaster / 1000) / transcalency_wall_plaster) + 0.133 + 0.05;
        double koefOfHeatLossResistanceHouse = 1 / heatLossResistanceWall * 4.19;
        stateOfTimeHeatLoss = (lenghtWall * widthWall * heightWall * 1.29 * 1005) / (koefOfHeatLossResistanceHouse * 1000 * (lenghtWall * 2 + widthWall * 2) * heightWall);
        return stateOfTimeHeatLoss;
    }

    public double getSquareHome() {
        return squareHome;
    }

    public void setSquareHome(double squareHome) {
        this.squareHome = squareHome;
        this.lenghtWall = this.widthWall = Math.sqrt(squareHome);
    }

    public double getHeightHome() {
        return heightHome;
    }

    public void setHeightHome(double heightHome) {
        this.heightHome = heightHome;
        this.heightWall = heightHome;
    }

    public boolean isOutputIsReady() {
        return outputIsReady;
    }

    public void setOutputIsReady(boolean outputIsReady) {
        this.outputIsReady = outputIsReady;
    }

    public List<MonthViewModel> getMonthViewModels() {
        return monthViewModels;
    }

    public void setMonthViewModels(List<MonthViewModel> monthViewModels) {
        this.monthViewModels = monthViewModels;
    }

    public double getInsideTemperature2() {
        return insideTemperature2;
    }

    public void setInsideTemperature2(double insideTemperature2) {
        this.insideTemperature2 = insideTemperature2;
    }

    public double getOutsideTemperature2() {
        return outsideTemperature2;
    }

    public void setOutsideTemperature2(double outsideTemperature2) {
        this.outsideTemperature2 = outsideTemperature2;
    }

    public double getNeedTemperature2() {
        return needTemperature2;
    }

    public void setNeedTemperature2(double needTemperature2) {
        this.needTemperature2 = needTemperature2;
    }

    public DayViewModel getOneDay() {
        return oneDay;
    }

    public void setOneDay(DayViewModel oneDay) {
        this.oneDay = oneDay;
    }

    public boolean isOutputIsReady2() {
        return outputIsReady2;
    }

    public void setOutputIsReady2(boolean outputIsReady2) {
        this.outputIsReady2 = outputIsReady2;
    }

    public double getNeedInsideTemperature() {
        return needInsideTemperature;
    }

    public void setNeedInsideTemperature(double needInsideTemperature) {
        this.needInsideTemperature = needInsideTemperature;
    }
    
    
    
}
