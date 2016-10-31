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
public enum SNMPClientDataType {
    TEMPERATURE("Температурный датчик"),VOLUME("Датчик объема"),CO_METTER("Датчик СО"),DEFAULT("Не назначено");
    String about;

    private SNMPClientDataType(String about) {
        this.about = about;
    }

    
    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
    
}
