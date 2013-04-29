package com.lexisnexis.resources;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hpccsystems.ui.constants.Constants;

public class GlobalVarsUI {
	
	public void addChildControls(Shell shell){
		
		Composite compositeForConcept = new Composite(shell, SWT.NONE);
		GridLayout layout = new GridLayout();
	    layout.numColumns = 2;
	    layout.makeColumnsEqualWidth = true;
	    compositeForConcept.setLayout(layout);
	    
	    GridData data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.verticalAlignment = GridData.FILL;
	    data.horizontalSpan = 2;
	    data.grabExcessHorizontalSpace = true;
	    data.grabExcessVerticalSpace = true;
	    compositeForConcept.setLayoutData(data);
	    
	    Label labelFileName = new Label(compositeForConcept, SWT.NONE);
	    labelFileName.setText("Save Configuration AS:");
	    
	    final Text textFileName = new Text(compositeForConcept, SWT.BORDER);
	    textFileName.setText(PropertiesReader.getProperty("server.filename"));
	    data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.grabExcessHorizontalSpace = true;
	    textFileName.setLayoutData(data);
	    
	    Label labelServerUserName = new Label(compositeForConcept, SWT.NONE);
	    labelServerUserName.setText("Server UserName:");
	    
	    final Text textServerUserName = new Text(compositeForConcept, SWT.BORDER);
	    textServerUserName.setText(PropertiesReader.getProperty("server.username"));
	    data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.grabExcessHorizontalSpace = true;
	    textServerUserName.setLayoutData(data);
	    
	    Label labelServerPassword = new Label(compositeForConcept, SWT.NONE);
	    labelServerPassword.setText("Server Password:");
	    
	    final Text textServerPassword = new Text(compositeForConcept, SWT.BORDER);
	    textServerPassword.setEchoChar('*');
	    textServerPassword.setText(PropertiesReader.getProperty("server.password"));
	    data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.grabExcessHorizontalSpace = true;
	    textServerPassword.setLayoutData(data);
	    
	    Label labelServerHost = new Label(compositeForConcept, SWT.NONE);
	    labelServerHost.setText("Server Host:");
	    
	    final Text textServerHost = new Text(compositeForConcept, SWT.BORDER);
	    textServerHost.setText(PropertiesReader.getProperty("server.host"));
	    data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.grabExcessHorizontalSpace = true;
	    textServerHost.setLayoutData(data);
	    
	    Label labelLandingZoneDir = new Label(compositeForConcept, SWT.NONE);
	    labelLandingZoneDir.setText("Landing Zone Dir:");
	    
	    final Text textLandingZoneDir = new Text(compositeForConcept, SWT.BORDER);
	    textLandingZoneDir.setText(PropertiesReader.getProperty("landingzone.path"));
	    data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.grabExcessHorizontalSpace = true;
	    textLandingZoneDir.setLayoutData(data);
	    
	    
	    // Composite for holding Buttons(Ok, Cancel)
	    Composite comp = new Composite(shell, SWT.NONE);
	    layout = new GridLayout();
	    layout.numColumns = 2;
	    layout.makeColumnsEqualWidth = true;
	    comp.setLayout(layout);
	    
	    data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.verticalAlignment = GridData.FILL;
	    data.horizontalSpan = 3;
	    data.grabExcessHorizontalSpace = true;
	    comp.setLayoutData(data);
	    
	    Button btnOk = new Button(comp, SWT.PUSH);
	    btnOk.setText(Constants.BTN_OK);
		data = new GridData();
		data.horizontalAlignment = GridData.END;
		data.grabExcessHorizontalSpace = true;
		data.widthHint = 80;
		btnOk.setLayoutData(data);
		btnOk.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				Map<String, String> mapProperties = new HashMap<String, String>();
				mapProperties.put("filename", textFileName.getText());
				mapProperties.put("server.username", textServerUserName.getText());
				mapProperties.put("server.password", textServerPassword.getText());
				mapProperties.put("server.host", textServerHost.getText());
				mapProperties.put("landingzone.path", textLandingZoneDir.getText());
				
				writeToPropertiesFile(mapProperties);
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void writeToPropertiesFile(Map<String, String> mapProperties) {
		
		if(mapProperties != null && mapProperties.size() > 0) {
			Set<String> setKeys = mapProperties.keySet();
			PropertiesReader.setFileName(mapProperties.get("filename"));
			for (Iterator<String> iterator = setKeys.iterator(); iterator.hasNext();) {
				String key = iterator.next();
				PropertiesReader.setProperty(key, mapProperties.get(key));
				
			}
		}
	}
	
	public void run() {
		
		Display display = new Display();
	    Shell shell = new Shell(display);
	    shell.setText(Constants.ADD_CONCEPTS_TITLE);
	    shell.setSize(800, 550);
	    GridLayout layout = new GridLayout();
	    layout.numColumns = 3;
	    layout.marginLeft = 10;
	    layout.marginRight = 10;
	    layout.makeColumnsEqualWidth = true;
	    shell.setLayout(layout);
	    
	    addChildControls(shell);
		
	    shell.open();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    display.dispose();
	}
	
	public static void main(String[] args) {
		new GlobalVarsUI().run();
	}
	
}
