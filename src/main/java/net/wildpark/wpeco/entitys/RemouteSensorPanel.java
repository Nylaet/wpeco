/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wildpark.wpeco.entitys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Panker-RDP
 */
@Entity
public class RemouteSensorPanel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String address="some.domain.net";
    private String port="161";
    private String title="Default Remote SNMP Listener";
    @OneToMany(cascade = CascadeType.ALL)
    private List<SNMPClient> snmpClient;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SNMPClient> getSnmpClient() {
        if(snmpClient==null)snmpClient=new ArrayList<>();
        return snmpClient;
    }

    public void setSnmpClient(List<SNMPClient> snmpClient) {
        this.snmpClient = snmpClient;
    }
    
    public void removeSnmpClient(SNMPClient client){
        try{
            snmpClient.remove(client);
        }catch(IllegalArgumentException ex){
            System.out.println(ex);
        }
    }
    
    

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RemouteSensorPanel)) {
            return false;
        }
        RemouteSensorPanel other = (RemouteSensorPanel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.wildpark.wpeco.entitys.RemouteSensorPanel[ id=" + id + " ]";
    }
    
}
