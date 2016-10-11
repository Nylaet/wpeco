/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wildpark.wpeco.enums;

/**
 *
 * @author Panker-RDP
 */
public enum KettleModelWPEco {
    WPECO25("WPEco 25",25,25);
    KettleModelWPEco(String modelName,int furnaceSquare,int furnaceVolume){
        this.furnaceSquare=furnaceSquare;
        this.modelName=modelName;
        this.furnaceVolume=furnaceVolume;
    }
    
    String modelName;
    int furnaceSquare;
    int furnaceVolume;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getFurnaceSquare() {
        return furnaceSquare;
    }

    public void setFurnaceSquare(int furnaceSquare) {
        this.furnaceSquare = furnaceSquare;
    }

    public int getFurnaceVolume() {
        return furnaceVolume;
    }

    public void setFurnaceVolume(int furnaceVolume) {
        this.furnaceVolume = furnaceVolume;
    }
    
    
}
