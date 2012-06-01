/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclproject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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

import org.hpccsystems.eclguifeatures.*;

/**
 *
 * @author ChalaAX
 */
public class ECLProjectDialog extends JobEntryDialog implements JobEntryDialogInterface {

    private ECLProject jobEntry;
    
    private Text jobEntryName;
    
    private Text recordsetName;
  //  private inputs;
    private Combo declareCounter;
    private Combo inRecordName;
    private Text outRecordName;
    private Text outRecordFormat;
    private Text transformName;
    private Text transformFormat;
    private Text parameterName;
    
    
    private Button wOK, wCancel;
    private boolean backupChanged;
    private SelectionAdapter lsDef;

    public ECLProjectDialog(Shell parent, JobEntryInterface jobEntryInt, Repository rep, JobMeta jobMeta) {
        super(parent, jobEntryInt, rep, jobMeta);
        jobEntry = (ECLProject) jobEntryInt;
        if (this.jobEntry.getName() == null) {
            this.jobEntry.setName("Project");
        }
    }

    public JobEntryInterface open() {
        Shell parentShell = getParent();
        Display display = parentShell.getDisplay();

        shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
        
        props.setLook(shell);
        JobDialog.setShellImage(shell, jobEntry);
        
        
        String datasets[] = null;
        AutoPopulate ap = new AutoPopulate();
        try{
            //Object[] jec = this.jobMeta.getJobCopies().toArray();
            
            datasets = ap.parseDatasetsRecordsets(this.jobMeta.getJobCopies());
        }catch (Exception e){
            System.out.println("Error Parsing existing Datasets");
            System.out.println(e.toString());
            datasets = new String[]{""};
        }

        ModifyListener lsMod = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                jobEntry.setChanged();
            }
        };

        backupChanged = jobEntry.hasChanged();

        FormLayout formLayout = new FormLayout();
        //formLayout.marginWidth = Const.FORM_MARGIN; //5
        //formLayout.marginHeight = Const.FORM_MARGIN; //5
        
        shell.setLayout(formLayout);
        shell.setSize(750,550); //800 X 600 (width X Height)

        int middle = props.getMiddlePct(); //35. This value is defined in org.pentaho.di.core.Const.
        int margin = Const.MARGIN; //4. This value is defined in org.pentaho.di.core.Const.

        shell.setLayout(formLayout);
        shell.setText("Define an ECL Project");
        
        //Start of code for Tabs
        CTabFolder tabFolder = new CTabFolder (shell, SWT.BORDER);
        tabFolder.setSimple(false);
        FormData tabFolderData = new FormData();
        tabFolderData.height = 500;
        tabFolderData.width = 700;
        tabFolderData.top = new FormAttachment(0, 0);
        tabFolderData.left = new FormAttachment(0, 0);
        tabFolderData.right = new FormAttachment(100, 0);
        tabFolderData.bottom = new FormAttachment(100, 0);
        tabFolder.setLayoutData(tabFolderData);
        
        CTabItem item1 = new CTabItem(tabFolder, SWT.NONE);
        Composite compForGrp = new Composite(tabFolder, SWT.NONE);
        compForGrp.setLayout(new FormLayout());
        
        item1.setText ("General");
        item1.setControl(compForGrp);
        
        //End of code for Tabs
        
        //Define buttons since they are used for component alignment 
        wOK = new Button(compForGrp, SWT.PUSH);
        wOK.setText("OK");
        wCancel = new Button(compForGrp, SWT.PUSH);
        wCancel.setText("Cancel");
        
        FormLayout groupLayout = new FormLayout();
        groupLayout.marginWidth = 10;
        groupLayout.marginHeight = 10;

        // Stepname line
        Group generalGroup = new Group(compForGrp, SWT.SHADOW_NONE);
        props.setLook(generalGroup);
        generalGroup.setText("General Details");
        generalGroup.setLayout(groupLayout);
        
        FormData generalGroupFormat = new FormData();
        /*generalGroupFormat.top = new FormAttachment(0, margin);
        generalGroupFormat.width = 400;
        generalGroupFormat.height = 65;
        generalGroupFormat.left = new FormAttachment(middle, 0);*/
        
        generalGroupFormat.height = 65;
        generalGroupFormat.left = new FormAttachment(0, 5);
        generalGroupFormat.right = new FormAttachment(100, -5);
        generalGroup.setLayoutData(generalGroupFormat);
        
        jobEntryName = buildText("Job Entry Name", null, lsMod, middle, margin, generalGroup);

        //All other contols
        //Project Declaration
        Group distributeGroup = new Group(compForGrp, SWT.SHADOW_NONE);
        props.setLook(distributeGroup);
        distributeGroup.setText("Project Details");
        distributeGroup.setLayout(groupLayout);
        /*FormData datasetGroupFormat = new FormData();
        datasetGroupFormat.top = new FormAttachment(generalGroup, margin);
        datasetGroupFormat.width = 400;
        datasetGroupFormat.height = 400;
        datasetGroupFormat.left = new FormAttachment(middle, 0);
        distributeGroup.setLayoutData(datasetGroupFormat);*/
        
        FormData datasetGroupFormat = new FormData();
        datasetGroupFormat.top = new FormAttachment(generalGroup, 5);
        datasetGroupFormat.bottom = new FormAttachment(wOK, -5);
        datasetGroupFormat.left = new FormAttachment(generalGroup, 0, SWT.LEFT);
        datasetGroupFormat.right = new FormAttachment(generalGroup, 0, SWT.RIGHT);
        distributeGroup.setLayoutData(datasetGroupFormat);
        
        FormData data = new FormData(50, 25);
		data.right = new FormAttachment(50, 0);
		data.bottom = new FormAttachment(100, 0);
		wOK.setLayoutData(data);
		
		data = new FormData(50, 25);
		data.left = new FormAttachment(wOK, 5);
		data.bottom = new FormAttachment(wOK, 0, SWT.BOTTOM);
		wCancel.setLayoutData(data);

        recordsetName = buildText("Resulting Recordset", null, lsMod, middle, margin, distributeGroup);
        
        declareCounter = buildCombo("Declare Counter", recordsetName, lsMod, middle, margin, distributeGroup, new String[]{"no", "yes"});
        inRecordName = buildCombo("In Record Name", declareCounter, lsMod, middle, margin, distributeGroup,datasets);
        outRecordName = buildText("Out Record Name", inRecordName, lsMod, middle, margin, distributeGroup);
        outRecordFormat = buildMultiText("Out Record Format", outRecordName, lsMod, middle, margin, distributeGroup);
        transformName = buildText("Transform Name", outRecordFormat, lsMod, middle, margin, distributeGroup);
        parameterName = buildText("Parameter Name", transformName, lsMod, middle, margin, distributeGroup);
        
        transformFormat = buildMultiText("Transform Format", parameterName, lsMod, middle, margin, distributeGroup);
        
        //Code for second tab for Mapper
        CTabItem item2 = new CTabItem(tabFolder, SWT.NONE);
        Composite compForGrp2 = new Composite(tabFolder, SWT.NONE);
        //compForGrp2.setLayout(new FormLayout());
        
        item2.setText ("Public");
        item2.setControl(compForGrp2);
        
        GridLayout mapperCompLayout = new GridLayout();
        mapperCompLayout.numColumns = 1;
		GridData mapperCompData = new GridData();
		mapperCompData.grabExcessHorizontalSpace = true;
		compForGrp2.setLayout(mapperCompLayout);
		compForGrp2.setLayoutData(mapperCompData);
		
		//Create a DataSet
		String[] dataSetList = {"FirstName", "LastName", "Address", "City", "State", "Zip", "Telephone Number"};
		MainMapper objMainMapper = new MainMapper(compForGrp2, dataSetList);
        
        // Add listeners
        Listener cancelListener = new Listener() {

            public void handleEvent(Event e) {
                cancel();
            }
        };
        Listener okListener = new Listener() {

            public void handleEvent(Event e) {
                ok();
            }
        };

        wCancel.addListener(SWT.Selection, cancelListener);
        wOK.addListener(SWT.Selection, okListener);

        lsDef = new SelectionAdapter() {

            public void widgetDefaultSelected(SelectionEvent e) {
                ok();
            }
        };


        // Detect X or ALT-F4 or something that kills this window...

        shell.addShellListener(new ShellAdapter() {

            public void shellClosed(ShellEvent e) {
                cancel();
            }
        });
        


        //if (jobEntry.getJobName() != null) {
        //    jobEntryName.setText(jobEntry.getJobName());
        //}
        if (jobEntry.getName() != null) {
            jobEntryName.setText(jobEntry.getName());
        }
        

        if (jobEntry.getDeclareCounterString() != null) {
            declareCounter.setText(jobEntry.getDeclareCounterString());
        }
        if (jobEntry.getInRecordName() != null) {
            inRecordName.setText(jobEntry.getInRecordName());
        }
         if (jobEntry.getRecordsetName() != null) {
            recordsetName.setText(jobEntry.getRecordsetName());
        }
        if (jobEntry.getOutRecordName() != null) {
            outRecordName.setText(jobEntry.getOutRecordName());
        }
        if (jobEntry.getOutRecordFormat() != null) {
            outRecordFormat.setText(jobEntry.getOutRecordFormat());
        }
        
        if (jobEntry.getTransformName() != null) {
            transformName.setText(jobEntry.getTransformName());
        }
        if (jobEntry.getTransformFormat() != null) {
            transformFormat.setText(jobEntry.getTransformFormat());
        }
        if (jobEntry.getParameterName() != null) {
            parameterName.setText(jobEntry.getParameterName());
        }


        //shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        return jobEntry;

    }

    private Text buildText(String strLabel, Control prevControl,
            ModifyListener lsMod, int middle, int margin, Composite groupBox) {
        // label
        Label fmt = new Label(groupBox, SWT.RIGHT);
        fmt.setText(strLabel);
        props.setLook(fmt);
        FormData labelFormat = new FormData();
        labelFormat.left = new FormAttachment(0, 0);
        labelFormat.top = new FormAttachment(prevControl, margin);
        labelFormat.right = new FormAttachment(middle, -margin);
        fmt.setLayoutData(labelFormat);

        // text field
        Text text = new Text(groupBox, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        props.setLook(text);
        text.addModifyListener(lsMod);
        FormData fieldFormat = new FormData();
        fieldFormat.left = new FormAttachment(middle, 0);
        fieldFormat.top = new FormAttachment(prevControl, margin);
        fieldFormat.right = new FormAttachment(100, 0);
        text.setLayoutData(fieldFormat);

        return text;
    }

    private Text buildMultiText(String strLabel, Control prevControl,
            ModifyListener lsMod, int middle, int margin, Composite groupBox) {
        // label
        Label fmt = new Label(groupBox, SWT.RIGHT);
        fmt.setText(strLabel);
        props.setLook(fmt);
        FormData labelFormat = new FormData();
        labelFormat.left = new FormAttachment(0, 0);
        labelFormat.top = new FormAttachment(prevControl, margin);
        labelFormat.right = new FormAttachment(middle, -margin);
        fmt.setLayoutData(labelFormat);

        // text field
        Text text = new Text(groupBox, SWT.MULTI | SWT.LEFT | SWT.BORDER | SWT.V_SCROLL);
        props.setLook(text);
        text.addModifyListener(lsMod);
        FormData fieldFormat = new FormData();
        fieldFormat.left = new FormAttachment(middle, 0);
        fieldFormat.top = new FormAttachment(prevControl, margin);
        fieldFormat.right = new FormAttachment(100, 0);
        fieldFormat.height = 100;
        text.setLayoutData(fieldFormat);

        return text;
    }
    
    private Combo buildCombo(String strLabel, Control prevControl,
            ModifyListener lsMod, int middle, int margin, Composite groupBox, String[] items) {
        // label
        Label fmt = new Label(groupBox, SWT.RIGHT);
        fmt.setText(strLabel);
        props.setLook(fmt);
        FormData labelFormat = new FormData();
        labelFormat.left = new FormAttachment(0, 0);
        labelFormat.top = new FormAttachment(prevControl, margin);
        labelFormat.right = new FormAttachment(middle, -margin);
        fmt.setLayoutData(labelFormat);

        // combo field
        Combo combo = new Combo(groupBox, SWT.MULTI | SWT.LEFT | SWT.BORDER);
        props.setLook(combo);
        combo.setItems(items);
        combo.addModifyListener(lsMod);
        FormData fieldFormat = new FormData();
        fieldFormat.left = new FormAttachment(middle, 0);
        fieldFormat.top = new FormAttachment(prevControl, margin);
        fieldFormat.right = new FormAttachment(100, 0);
        fieldFormat.height = 50;
        combo.setLayoutData(fieldFormat);

        return combo;
    }

    private void ok() {

        jobEntry.setName(jobEntryName.getText());
        jobEntry.setDeclareCounterString(declareCounter.getText());
        jobEntry.setRecordsetName(recordsetName.getText()); 
        jobEntry.setInRecordName(inRecordName.getText()); 
        jobEntry.setOutRecordName(outRecordName.getText());
        jobEntry.setOutRecordFormat(outRecordFormat.getText());
        jobEntry.setTransformName(transformName.getText());
        jobEntry.setTransformFormat(transformFormat.getText());
        jobEntry.setParameterName(parameterName.getText());
        dispose();
    }

    private void cancel() {
        jobEntry.setChanged(backupChanged);
        jobEntry = null;
        dispose();
    }

    public void dispose() {
        WindowProperty winprop = new WindowProperty(shell);
        props.setScreen(winprop);
        shell.dispose();
    }
}
