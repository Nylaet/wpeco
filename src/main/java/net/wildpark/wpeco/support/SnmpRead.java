/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wildpark.wpeco.support;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SnmpRead {
    TransportMapping transportMapping;
    Snmp snmp;
    PDU pdu;
    public String getTestSnmpReceiving(int snmpVersion, int retry, String community, String host, String request) {
        //Setting Up pdu
        pdu=new PDU();
        pdu.add(new VariableBinding(new OID(request)));
        pdu.setType(PDU.GET);
        //Setting UpComunity target
        CommunityTarget communityTarget=new CommunityTarget(new UdpAddress(host), new OctetString(community));
        communityTarget.setVersion(snmpVersion);
        try {
            //Sending request
            snmp=new Snmp(new DefaultUdpTransportMapping());
            snmp.listen();
            
            ResponseEvent event=snmp.send(pdu, communityTarget);
            if(event.getResponse()==null){
                snmp.close();
                System.out.println("Timeout snmp");
            }
            else{
                //vent.getResponse().get
                System.out.println(event.getResponse().toString());
            }
        } catch (IOException ex) {
            
        }
        return "";
    }
}
