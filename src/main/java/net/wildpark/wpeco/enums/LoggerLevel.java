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
public enum LoggerLevel {
    FATAL("Фатальная ошибка"),ERROR("Ошибка"),WARN("Предупреждение"),INFO("Информация"),DEBUG("Отладочная информация");
    private String about;
    
    LoggerLevel(String about) {
        this.about=about;
    }
    
    public String getAbout(){
        return about;
    }
    
}
