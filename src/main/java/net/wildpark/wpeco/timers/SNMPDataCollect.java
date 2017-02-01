/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wildpark.wpeco.timers;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
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
@Stateless
@Startup
@Singleton
public class SNMPDataCollect {

    @EJB
    LogFacade logFacade;
    @EJB
    RemouteSensorPanelFacade rspf;
    Snmp snmp;
    PDU pdu;
    private boolean run = false;

    @Schedule(dayOfWeek = "*", hour = "*", minute = "*")
    
    public void myTimer() {
        try {
            if (run) {
                logFacade.create(new Log("Long process not ended. This iteration of shedule be scipped", LoggerLevel.WARN));
                return;
            }
            run = true;
            List<RemouteSensorPanel> rsps = rspf.findAll();

            if (rsps.isEmpty()) {
                logFacade.create(new Log("База клиентов пустая. Таймер не работает.", LoggerLevel.WARN));
            } else {
                for (RemouteSensorPanel remouteSensorPanel : rspf.findAll()) {
                    for (SNMPClient sc : remouteSensorPanel.getSnmpClient()) {
                        SnmpRead read = new SnmpRead();
                        String value = "";
                        if ((value = read.getTestSnmpReceiving(1, 3, "public", "udp:" + remouteSensorPanel.getAddress() + "/" + remouteSensorPanel.getPort(), sc.getOid())).equals("Timeout snmp")) {
                            logFacade.create(new Log("Timeout snmp", LoggerLevel.WARN));
                        } else {
                            sc.addValue(Double.parseDouble(value));
                        }
                    }
                }
            }
            run = false;
        } catch (Exception exception) {
            logFacade.create(new Log("Unreported exception", exception, LoggerLevel.FATAL));
        } finally {
            run = false;
        }
    }

    private class SnmpRead {

        

        public String getTestSnmpReceiving(int snmpVersion, int retry, String community, String host, String request) throws IOException {
            //Setting Up pdu
            pdu = new PDU();
            pdu.add(new VariableBinding(new OID(request)));
            pdu.setType(PDU.GET);
            //Setting UpComunity target
            Address address = GenericAddress.parse(host);
            CommunityTarget communityTarget = new CommunityTarget();
            communityTarget.setAddress(address);
            communityTarget.setCommunity(new OctetString("wpeco"));
            communityTarget.setVersion(snmpVersion);
            communityTarget.setRetries(3);
            communityTarget.setTimeout(1000);

            //Sending request
            DefaultUdpTransportMapping transportMapping=new DefaultUdpTransportMapping();
            Snmp snmp=new  Snmp(transportMapping);
            snmp.listen();

            ResponseEvent event = snmp.send(pdu, communityTarget);
            if (event.getResponse() == null) {
                
                snmp.close();
                transportMapping.close();
                return ("Timeout snmp");
            } else {
                String value = event.getResponse().getVariable(new OID(request)).toString();
                snmp.close();
                transportMapping.close();
                return value;
            }

        }
    }
}
