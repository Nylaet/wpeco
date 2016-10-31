/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wildpark.wpeco.facades;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.wildpark.wpeco.entitys.Log;

/**
 *
 * @author Panker-RDP
 */
@Stateless
public class LogFacade extends AbstractFacade<Log> {

    @PersistenceContext(unitName = "net.wildpark_WPEco2")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LogFacade() {
        super(Log.class);
    }
    
}
