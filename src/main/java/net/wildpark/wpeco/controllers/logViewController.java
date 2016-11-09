/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wildpark.wpeco.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import net.wildpark.wpeco.entitys.Log;
import net.wildpark.wpeco.enums.LoggerLevel;
import net.wildpark.wpeco.facades.LogFacade;

/**
 *
 * @author Panker-RDP
 */
@Named(value = "logController")
@RequestScoped
public class logViewController {

    private List<Log> logs = new ArrayList<>();
    @EJB
    private LogFacade logFacade;

    public logViewController() {
    }

    public List<Log> getLogs() {
        try {
            logs = logFacade.findAll();
        } catch (Exception exception) {
            logFacade.create(new Log("Ошибка при пропытке получить логи в массив", exception, LoggerLevel.FATAL));
        }
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public String getFormatedDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        if (date == null) {
            return "Не задано";
        }
        return sdf.format(date);
    }

}
