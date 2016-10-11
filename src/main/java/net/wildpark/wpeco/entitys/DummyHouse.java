package net.wildpark.wpeco.entitys;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DummyHouse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private final String title="Виртуальный дом";
    private final int thickness_wall_brick=640;//Толщина стены кирпичной мм
    private final double transcalency_wall_brick=0.7;//Коэффициент теплопроводности стены кирпичной ккал/м
    private final int thickness_wall_plaster=15;//Толщина стены штукатурка мм
    private final double transcalency_wall_plaster=0.6;//Коэфициент теплопроводности стены штукатурка ккал/м
    private final double transcalency_resistance_roof_floor=0.133;//Сопротивление теплопереходу пола и потолка м.кв*час*град/ккал
    private final double transcalency_resistance_outDoor_wall=0.005;//Сопротивление теплопереходу наружных стен м.кв*час*град/ккал
    private final double heat_capacity_air=1.005;//Удельная теплоемкость воздуха
    private double stateOfTimeHeatLoss;//Постоянная времени обогреваемого помещения
    private int outsideTemperature;//Температура забортная
    private int insideTemperature;//Температура внутри
    private int lenghtWall;
    private int widthWall;//Габариты помещения м.
    private int heightWall;
    public Long getId() {
        return id;
    }
    
    public double getHeatLoss(){
        double heatLoss=0.0;
        double heatLossResistanceWall=(thickness_wall_brick/1000)/transcalency_wall_brick+((thickness_wall_plaster/1000)/transcalency_wall_plaster)*4.19;
        double koefOfHeatLossResistanceHouse=1/(transcalency_resistance_roof_floor+heatLossResistanceWall+transcalency_resistance_outDoor_wall);
        stateOfTimeHeatLoss=(lenghtWall*widthWall*heightWall*1.29*1005)/(koefOfHeatLossResistanceHouse*1000*(lenghtWall*2+widthWall*2)*heightWall);
        heatLoss=lenghtWall*widthWall*heightWall*1.29*1005+(koefOfHeatLossResistanceHouse*1000*(lenghtWall*2+widthWall*2)*heightWall)*(insideTemperature-outsideTemperature);
        return heatLoss;
    }
    
    public double transferFunction(double incomingHeat){
        return 0.0;
    }

    public double getHeatIrradiation(int inWarm){
        double heatIrradiation=0.0;
        
        return heatIrradiation;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DummyHouse)) {
            return false;
        }
        DummyHouse other = (DummyHouse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.wildpark.wpeco.entitys.DummyHouse[ id=" + id + " ]";
    }
    
}
