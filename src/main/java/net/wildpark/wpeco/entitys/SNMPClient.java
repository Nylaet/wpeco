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
import net.wildpark.wpeco.enums.SNMPClientDataType;

/**
 *
 * @author Panker-RDP
 */
@Entity
public class SNMPClient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String oid;
    private String name;
    private SNMPClientDataType dataType;
    @OneToMany(cascade = CascadeType.ALL)
    private List<DataHistory> dataHistory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SNMPClientDataType getDataType() {
        return dataType;
    }

    public void setDataType(SNMPClientDataType dataType) {
        this.dataType = dataType;
    }
    
    
    public List<DataHistory> getDataHistory() {
        if(dataHistory==null)dataHistory=new ArrayList<>();
        return dataHistory;
    }

    public void setDataHistory(List<DataHistory> dataHistory) {
        this.dataHistory = dataHistory;
    }
    
    public void addValue(double value){
        getDataHistory().add(new DataHistory(value));
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
        if (!(object instanceof SNMPClient)) {
            return false;
        }
        SNMPClient other = (SNMPClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.wildpark.wpeco.entitys.SNMPClient[ id=" + id + " ]";
    }
    
}
