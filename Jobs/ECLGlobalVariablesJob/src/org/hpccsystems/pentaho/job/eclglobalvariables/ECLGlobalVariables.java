/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclglobalvariables;

import java.util.ArrayList;
import java.util.List;
import org.pentaho.di.cluster.SlaveServer;
import org.pentaho.di.compatibility.Value;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.job.entry.JobEntryBase;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.w3c.dom.Node;

/**
 *
 * @author ChalaAX
 */
public class ECLGlobalVariables extends JobEntryBase implements Cloneable, JobEntryInterface {
    
    //private String jobName;
    //private String name = "";
    
    private String serverIP = "";
    private String serverPort = "";
    
    private String landingZone = "";

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getLandingZone() {
        return landingZone;
    }

    public void setLandingZone(String landingZone) {
        this.landingZone = landingZone;
    }
    
    

    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
        
        Result result = prevResult;
        

        return result;
    }

    @Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
        try {
            super.loadXML(node, list, list1);
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"server_ip")) != null)
                this.setServerIP(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"server_ip")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"server_port")) != null)
                this.setServerPort(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"server_port")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"landing_zone")) != null)
                this.setLandingZone(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"landing_zone")));
            
            
        } catch (Exception e) {
            throw new KettleXMLException("ECL Distribute Job Plugin Unable to read step info from XML node", e);
        }

    }



    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
        
        retval += "             <server_ip>"+this.serverIP+"</server_ip>"+Const.CR;
        
        retval += "             <server_port>"+this.serverPort+"</server_port>"+Const.CR;
        
        retval += "             <landing_zone>"+this.landingZone+"</landing_zone>"+Const.CR;
        
       
       
       
       
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
            //jobName = rep.getStepAttributeString(id_jobentry, "jobName"); //$NON-NLS-1$

            //name = rep.getStepAttributeString(id_jobentry, "name"); //$NON-NLS-1$
            serverIP = rep.getStepAttributeString(id_jobentry, "server_ip");
            
            serverPort = rep.getStepAttributeString(id_jobentry, "server_port");
            landingZone = rep.getStepAttributeString(id_jobentry, "landing_zone");
                        
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {

            
            rep.saveStepAttribute(id_job, getObjectId(), "server_ip", serverIP);
            rep.saveStepAttribute(id_job, getObjectId(), "server_port", serverPort);
            rep.saveStepAttribute(id_job, getObjectId(), "landing_zone", landingZone);
        
        } catch (Exception e) {
            throw new KettleException("Unable to save info into repository" + id_job, e);
        }
    }

    public boolean evaluates() {
        return true;
    }

    public boolean isUnconditional() {
        return true;
    }
}
