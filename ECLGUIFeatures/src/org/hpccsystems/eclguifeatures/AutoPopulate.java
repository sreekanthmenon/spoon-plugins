/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.eclguifeatures;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entry.JobEntryDialogInterface;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.ui.core.gui.WindowProperty;
import org.pentaho.di.ui.job.dialog.JobDialog;
import org.pentaho.di.ui.job.entry.JobEntryDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;


import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.job.JobHopMeta;

import org.pentaho.di.job.entry.JobEntryCopy;

/**
 *
 * @author ChambeJX
 */
public class AutoPopulate {
    
    private String[] dataSets;
    private String[] recordSets;
    
    
    
    public String[] parseDatasets(List<JobEntryCopy> jobs) throws Exception{
        System.out.println(" ------------ parseDataSet ------------- ");
        String datasets[] = null;
        ArrayList<String> adDS = new ArrayList<String>();
       
        Object[] jec = jobs.toArray();

        int k = 0;

        for(int j = 0; j<jec.length; j++){
            //System.out.println("Node(i): " + j + " | " +((JobEntryCopy)jec[j]).getName());

            if(!((JobEntryCopy)jec[j]).getName().equalsIgnoreCase("START") && !((JobEntryCopy)jec[j]).getName().equalsIgnoreCase("OUTPUT") && !((JobEntryCopy)jec[j]).getName().equalsIgnoreCase("SUCCESS")){
                //System.out.println("Node(k): " + k);
                adDS.add((String)((JobEntryCopy)jec[j]).getName());
                k++;
            }

            System.out.println(((JobEntryCopy)jec[j]).getXML());

        }
        //saving the loop code using arraylists
        datasets = adDS.toArray(new String[k]);


        
        return datasets;
    }
    
}
