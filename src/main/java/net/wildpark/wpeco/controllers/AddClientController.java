/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wildpark.wpeco.controllers;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import net.wildpark.wpeco.entitys.RemouteSensorPanel;
import net.wildpark.wpeco.entitys.SNMPClient;
import net.wildpark.wpeco.facades.RemouteSensorPanelFacade;

/**
 *
 * @author Panker-RDP
 */
@Named(value = "addClient")
@SessionScoped
public class AddClientController implements Serializable {
    
    private RemouteSensorPanel created=new RemouteSensorPanel();
    private List<SNMPClient> snmpcs=new ArrayList<>();
    @EJB
    RemouteSensorPanelFacade panelFacade;
    private List<RemouteSensorPanel> rsps=new ArrayList<>();
    public AddClientController() {
        if(snmpcs.isEmpty()){
            SNMPClient first=new SNMPClient();
            snmpcs.add(first);
        }
    }

    public RemouteSensorPanel getCreated() {
        return created;
    }

    public void setCreated(RemouteSensorPanel created) {
        this.created = created;
    }

    public List<SNMPClient> getSnmpcs() {
        return snmpcs;
    }

    public void setSnmpcs(List<SNMPClient> snmpcs) {
        this.snmpcs = snmpcs;
    }
    
    public void addSnmpClient(){
        SNMPClient addedClient=new SNMPClient();
        snmpcs.add(addedClient);
    }
    
    public void removeSnmpClient(SNMPClient client){
        snmpcs.remove(client);
    }
    
    public void save(){
        if(created.getAddress()!=null&&created.getTitle()!=null){
            if(!snmpcs.isEmpty()){
                SNMPClient removeElement=new SNMPClient();
                boolean haveEmptyRecords=true;
                while(haveEmptyRecords){
                    haveEmptyRecords=false;
                    for (SNMPClient snmpc : snmpcs) {
                        if(snmpc.getOid()==null||snmpc.getOid().isEmpty()){
                            removeElement=snmpc;
                            haveEmptyRecords=true;
                        }
                    }
                    if(haveEmptyRecords){
                        snmpcs.remove(removeElement);
                    }
                }
                created.setSnmpClient(snmpcs);
                panelFacade.create(created);
                created=new RemouteSensorPanel();
                snmpcs.clear();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Успех! Можно перейти к просмотру!"));
                FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("inputForm");
            }
        }
    }
    
    public void saveCurrent(RemouteSensorPanel rsp){
        panelFacade.edit(rsp);
    }
    
    public void addSnmpClientToCurrent(RemouteSensorPanel rsp){
        SNMPClient client=new SNMPClient();
        rsp.getSnmpClient().add(client);
        panelFacade.edit(rsp);
    }
    
    public List<RemouteSensorPanel> getRSPs(){
        if(rsps.isEmpty()){
            try{
                rsps=panelFacade.findAll();
            }catch(NullPointerException ex){
                
            }
        }        
        return rsps;
    }
    
    
    
}
