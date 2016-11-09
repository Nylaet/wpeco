/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wildpark.wpeco.controllers;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import net.wildpark.wpeco.entitys.Log;
import net.wildpark.wpeco.entitys.RemouteSensorPanel;
import net.wildpark.wpeco.entitys.SNMPClient;
import net.wildpark.wpeco.enums.LoggerLevel;
import net.wildpark.wpeco.facades.LogFacade;
import net.wildpark.wpeco.facades.RemouteSensorPanelFacade;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 *
 * @author Panker-RDP
 */
@Named(value = "addClient")
@SessionScoped
public class AddClientController implements Serializable {

    private RemouteSensorPanel created = new RemouteSensorPanel();
    private List<SNMPClient> snmpcs = new ArrayList<>();
    @EJB
    RemouteSensorPanelFacade panelFacade;
    private List<RemouteSensorPanel> rsps = new ArrayList<>();
    @EJB
    LogFacade logFacade;
    public AddClientController() {
        if (snmpcs.isEmpty()) {
            SNMPClient first = new SNMPClient();
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

    public void addSnmpClient() {
        SNMPClient addedClient = new SNMPClient();
        snmpcs.add(addedClient);
    }

    public void removeSnmpClient(SNMPClient client) {
        snmpcs.remove(client);
    }

    public void save() {
        if (created.getAddress() != null && created.getTitle() != null) {
            if (!snmpcs.isEmpty()) {
                SNMPClient removeElement = new SNMPClient();
                boolean haveEmptyRecords = true;
                while (haveEmptyRecords) {
                    haveEmptyRecords = false;
                    for (SNMPClient snmpc : snmpcs) {
                        if (snmpc.getOid() == null || snmpc.getOid().isEmpty()) {
                            removeElement = snmpc;
                            haveEmptyRecords = true;
                        }
                    }
                    if (haveEmptyRecords) {
                        snmpcs.remove(removeElement);
                    }
                }
                created.setSnmpClient(snmpcs);
                panelFacade.create(created);
                created = new RemouteSensorPanel();
                snmpcs.clear();
                logFacade.create(new Log("Создана новая единица мониторинга",LoggerLevel.INFO));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Успех! Можно перейти к просмотру!"));
                FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("inputForm");
            }
        }
    }

    public void saveCurrent(RemouteSensorPanel rsp) {
        logFacade.create(new Log("Обновлена единица мониторинга",LoggerLevel.INFO));
        panelFacade.edit(rsp);
    }

    public void addSnmpClientToCurrent(RemouteSensorPanel rsp) {
        SNMPClient client = new SNMPClient();
        rsp.getSnmpClient().add(client);
        panelFacade.edit(rsp);
    }

    public List<RemouteSensorPanel> getRSPs() {
        if (rsps.isEmpty()) {
            try {
                rsps = panelFacade.findAll();
            } catch (NullPointerException ex) {

            }
        }
        return rsps;
    }

    public String getUpTime(RemouteSensorPanel rsp) {
        String request="1.3.6.1.2.1.1.3.0";
        String value=getTestSnmpReceiving(1, 3, "public", "udp:"+rsp.getAddress()+"/"+rsp.getPort(), request);
        return value;
    }

    public String getTestSnmpReceiving(int snmpVersion, int retry, String community, String host, String request) {
        TransportMapping transportMapping;
        Snmp snmp;
        PDU pdu;
        //Setting Up pdu
        pdu = new PDU();
        pdu.add(new VariableBinding(new OID(request)));
        pdu.setType(PDU.GET);
        //Setting UpComunity target
        Address address = GenericAddress.parse(host);
        CommunityTarget communityTarget = new CommunityTarget();
        communityTarget.setAddress(address);
        communityTarget.setCommunity(new OctetString("public"));
        communityTarget.setVersion(snmpVersion);
        communityTarget.setRetries(3);
        communityTarget.setTimeout(1000);
        try {
            //Sending request
            snmp = new Snmp(new DefaultUdpTransportMapping());
            snmp.listen();

            ResponseEvent event = snmp.send(pdu, communityTarget);
            if (event.getResponse() == null) {
                snmp.close();
                return ("Timeout snmp");
            } else {
                String value = event.getResponse().getVariable(new OID(request)).toString();
                System.out.println(value);
                return value;
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return "";
    }

}
