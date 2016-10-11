package net.wildpark.wpeco.entitys;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import net.wildpark.wpeco.enums.KettleModelWPEco;

@Entity
public class Kettle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private final int NET_CALORIFIC_VALUE=4200;
    private KettleModelWPEco modelWPEco;
    private int fuelConsumpitonPerHour;
    private int inputTemperature;
    private int outputTemperature;
    private int efficiencyKettle;
    private int volumeCoolant=Integer.MAX_VALUE;
    private int airConsumpitonPerHour;
    private int sensorCO;
    
    /*
        Узнать у Бойченко:
    1. Объем топок моделей
    2. Площадь поверхностей топок 
    */
    
    public int getThermapPowerKettle(){//Теплова мощность топки
        return fuelConsumpitonPerHour*NET_CALORIFIC_VALUE;        
    }
    
    public int furnaceSpecificVolumeticCapacity(){// Удельная объемная мощность топки
        return getThermapPowerKettle()/modelWPEco.getFurnaceVolume();
    }
    
    public int furnaceSpecificThermalCapacityGride(){//Удельная тепловая мощность решетки горелки
        return getThermapPowerKettle()/modelWPEco.getFurnaceSquare();
    }
    
    public int getVolumeCoolant(){//Испытательный расчет объема теплоносителя.
        int temporalCoolnat=(efficiencyKettle*getThermapPowerKettle())/(100*(outputTemperature-inputTemperature));
        if(temporalCoolnat<volumeCoolant)volumeCoolant=temporalCoolnat;
        return volumeCoolant;
    }
    
    
    
    public Long getId() {
        return id;
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
        if (!(object instanceof Kettle)) {
            return false;
        }
        Kettle other = (Kettle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.wildpark.wpeco.entitys.Kettle[ id=" + id + " ]";
    }
    
}
