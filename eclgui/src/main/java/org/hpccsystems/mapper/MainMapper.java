package org.hpccsystems.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

public class MainMapper {
	
	// The table viewer
	private TableViewer tableViewer;
	// Create a RecordList and assign it to an instance variable
	private RecordList recordList = new RecordList();
	
	private Text txtVariableName;
	private Text txtExpression;
	
	private String[] functionList = null;
	private String[] dataSetList = null;
	private String[] operatorList = null;
	
	//The Constructor has input as 
	public MainMapper(Shell shell, String[] dataSetList){
		setDataSetList(dataSetList);
		populateFunctionList();
		populateOperatorList();
		this.addChildControls(shell);
	}
	
	/**
	 * Create a new shell, add the widgets, open the shell
	 * @return the shell that was created	 
	 */
	private void addChildControls(Shell shell) {
		Composite tblComposite = new Composite(shell, SWT.NONE);
		createTable(tblComposite);		// Create the table
		createButtons(tblComposite);
		buildExpressionPanel(shell);	// Add the widgets needed to build a Expression Panel
		
	}
	
	private void populateFunctionList() {
		String[] fuctionList = {"ABS", "ACOS", "AGGREGATE", "ALLNODES", "APPLY"};
		setFunctionList(fuctionList);
	}
	
	private void populateOperatorList() {
		String[] operatorList = {"+", "-", "*", "/", "%", "||", "(", ")", "=", "<>", ">", "<", "<=", ">="};
		setOperatorList(operatorList);
	}
	
	public String[] getFunctionList() {
		return functionList;
	}

	public void setFunctionList(String[] functionList) {
		this.functionList = functionList;
	}

	public String[] getDataSetList() {
		return dataSetList;
	}

	public void setDataSetList(String[] dataSetList) {
		this.dataSetList = dataSetList;
	}
	
	public String[] getOperatorList() {
		return operatorList;
	}

	public void setOperatorList(String[] operatorList) {
		this.operatorList = operatorList;
	}

	private void createTable(Composite tblComposite) {
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		GridData data = new GridData(GridData.FILL_BOTH);
		data.heightHint = 250;
		tblComposite.setLayout(layout);
		tblComposite.setLayoutData(data);
		
		int style = SWT.CHECK | SWT.FULL_SELECTION | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL;
		final Table table = new Table(tblComposite, style);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 3;
		table.setLayoutData(gridData);	
		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		//1St Column - Output
		final TableColumn tableColumn0 = new TableColumn(table, SWT.LEFT, 0);
		tableColumn0.setImage(MapperLabelsProvider.getImage("unchecked"));
		tableColumn0.setText("Variable");
		tableColumn0.setWidth(200);
		
		// 3rd column - Expression
		TableColumn column = new TableColumn(table, SWT.LEFT, 1);
		column.setText("Expression");
		column.setWidth(400);
		
		tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(new MapperContentProvider());	//Set the Content Provider for the table	
		tableViewer.setLabelProvider(new MapperLabelsProvider());	//Set the Label Provider for the table
		
		tableViewer.setInput(recordList);	//Add an empty RecordList to the TableViewer
		
		tableColumn0.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
		        boolean checkBoxFlag = false;
		        for (int i = 0; i < table.getItemCount(); i++) {
		            if (table.getItems()[i].getChecked()) {
		                checkBoxFlag = true;
		            }
		        }
		        if (checkBoxFlag) {
		            for (int m = 0; m < table.getItemCount(); m++) {
		                table.getItems()[m].setChecked(false);
		                tableColumn0.setImage(MapperLabelsProvider.getImage("unchecked"));
		                table.deselectAll();
		            }
		        } else {
		            for (int m = 0; m < table.getItemCount(); m++) {
		                table.getItems()[m].setChecked(true);
		                tableColumn0.setImage(MapperLabelsProvider.getImage("checked"));
		                table.selectAll();
		            }
		        } //end of else
		    } //end of handleEvent function
		});
	}
	
	/**
	 * Add the Buttons
	 * @param parent
	 */
	private void createButtons(Composite tblComposite) {
		 // Create and configure the "Add" button
		Button edit = new Button(tblComposite, SWT.PUSH | SWT.CENTER);
		edit.setText("Edit");
		GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		edit.setLayoutData(gridData);
		edit.addSelectionListener(new SelectionAdapter() {
       		// Add a record and refresh the view
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = 0;
				if(tableViewer.getTable().getItemCount() >0 ){
					for (int i = 0; i < tableViewer.getTable().getItemCount(); i++) {
						if (tableViewer.getTable().getItems()[i].getChecked()) {
							selectionIndex = i;
						}
					}
					if(tableViewer.getTable().getItem(selectionIndex).getChecked()){
						MapperBO objRecord = recordList.getRecord(selectionIndex);
						txtVariableName.setText(objRecord.getOpVariable());
						txtExpression.setText(objRecord.getExpression());
						txtExpression.setFocus();
					}
				}
			}
		});
		
		Button delete = new Button(tblComposite, SWT.PUSH | SWT.CENTER);
		delete.setText("Delete");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		delete.setLayoutData(gridData);
		delete.addSelectionListener(new SelectionAdapter() {
			//Remove all the records that are checked and refresh the view
			public void widgetSelected(SelectionEvent e) {
				List<Integer> arlCheckedIndexes = new ArrayList<Integer>();
				for (int i = 0; i < tableViewer.getTable().getItemCount(); i++) {
					if (tableViewer.getTable().getItems()[i].getChecked()) {
						arlCheckedIndexes.add(i);
					}
				}
						
				Collections.sort(arlCheckedIndexes);
						
				Integer[] arrSortedIndexes = arlCheckedIndexes.toArray(new Integer[arlCheckedIndexes.size()]);
				for (int j = arrSortedIndexes.length - 1 ; j>=0; j--) {
					recordList.removeRecord(arrSortedIndexes[j]);
				}
				tableViewer.refresh();
				tableViewer.getTable().getColumns()[0].setImage(MapperLabelsProvider.getImage("unchecked"));
			}
		});
		
	}
	
	public void buildExpressionPanel(Shell shell){
		
		Composite comp2 = new Composite(shell, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		GridData data = new GridData(GridData.FILL_BOTH);
		data.heightHint = 400;
		comp2.setLayout(layout);
		comp2.setLayoutData(data);
		
		Group group1 = new Group(comp2, SWT.SHADOW_IN);
	    group1.setText("ECL Expression Builder");
	    layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = true;
		data = new GridData(GridData.FILL_BOTH);
		group1.setLayout(layout);
		group1.setLayoutData(data);
		
		Composite compVariable = new Composite(group1, SWT.NONE);
		layout = new GridLayout();
		//layout.marginLeft = 0;
		layout.numColumns = 2;
		data = new GridData();
		data.horizontalSpan = 2;
		compVariable.setLayout(layout);
		compVariable.setLayoutData(data);
		
		Label lblVariableName = new Label(compVariable, SWT.NONE);
		lblVariableName.setText("Variable Name:");
		GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		lblVariableName.setLayoutData(gridData);
		
		txtVariableName = new Text(compVariable, SWT.SINGLE | SWT.BORDER );
		gridData = new GridData (GridData.FILL_HORIZONTAL);
		gridData.widthHint = 280;
		txtVariableName.setLayoutData(gridData);
		
		Composite compTreePanel = new Composite(group1, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 3;
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		compTreePanel.setLayout(layout);
		compTreePanel.setLayoutData(data);
		
		Label lblInput = new Label(compTreePanel, SWT.NONE);
		lblInput.setText("Input:");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		lblInput.setLayoutData(gridData);
		
		Label lblFunctions = new Label(compTreePanel, SWT.NONE);
		lblFunctions.setText("Functions:");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		lblFunctions.setLayoutData(gridData);
		
		Label lblOperators = new Label(compTreePanel, SWT.NONE);
		lblOperators.setText("Operators:");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		lblOperators.setLayoutData(gridData);
		
		Tree treeInputDataSet = new Tree(compTreePanel, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
	    gridData.heightHint = 100;
	    treeInputDataSet.setLayoutData(gridData);
	    Utils.fillTree(treeInputDataSet);
	    treeInputDataSet.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				//Do Nothing
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
				//Do Nothing
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Tree selectedTree = (Tree)e.widget;
				if(selectedTree.getSelection()[0].getParentItem() != null){
					String dataField = ((Tree)e.widget).getSelection()[0].getText();
					StringBuilder txtBuilder = new StringBuilder();
					txtBuilder.append(txtExpression.getText());
					txtExpression.setText(txtBuilder.append(dataField).toString());
				}
			}
		});
		
	    Tree treeFunctions = new Tree(compTreePanel, SWT.SINGLE | SWT.BORDER);
	    gridData = new GridData(GridData.FILL_HORIZONTAL);
	    gridData.heightHint = 100;
	    treeFunctions.setLayoutData(gridData);
	    Map<String, List<String>> mapFunctionValues = Utils.getFunctionValueMap();
	    Utils.fillTreeForFunctions(treeFunctions, mapFunctionValues);
	    treeFunctions.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				//Do Nothing
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
				//Do Nothing
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Tree selectedTree = (Tree)e.widget;
				if(selectedTree.getSelection()[0].getParentItem() != null){
					String dataField = ((Tree)e.widget).getSelection()[0].getText();
					if(txtExpression.getSelectionText() != null && txtExpression.getSelectionText().length() > 0) {
						String txtToBeReplaced = dataField+"( "+txtExpression.getSelectionText()+" )";
						StringBuffer buf = new StringBuffer(txtExpression.getText());
						buf.replace(txtExpression.getSelection().x, txtExpression.getSelection().y, txtToBeReplaced);
						txtExpression.setText(buf.toString());
					} else {
						StringBuffer buf = new StringBuffer(txtExpression.getText());
						buf.append(dataField+"()");
						txtExpression.setText(buf.toString());
					}
				}
			}
		});
	    
	    int style = SWT.FULL_SELECTION | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL;
	    final Table tblOperators = new Table(compTreePanel, style);
		gridData = new GridData();
		gridData.heightHint = 100;
		gridData.widthHint = 100;
		tblOperators.setLayoutData(gridData);
		
		for (int i = getOperatorList().length -1 ; i >= 0 ; i--) {
			TableItem item = new TableItem(tblOperators, SWT.NONE, 0);
			item.setText(getOperatorList()[i]);
		}
		
		tblOperators.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				//Do Nothing
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
				//Do Nothing
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				String dataField = ((Table)e.widget).getSelection()[0].getText();
				StringBuilder txtBuilder = new StringBuilder();
				txtBuilder.append(txtExpression.getText());
				txtExpression.setText(txtBuilder.append(dataField).toString());
			}
		});
		
		Label lblEclText = new Label(group1, SWT.NONE);
		lblEclText.setText("ECL Text:");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		lblEclText.setLayoutData(gridData);
		
		txtExpression = new Text(group1, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		gridData = new GridData (GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		txtExpression.setLayoutData(gridData);
		
		Button btnSave = new Button(group1, SWT.PUSH | SWT.CENTER);
		btnSave.setText("Add Expression");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 120;
		btnSave.setLayoutData(gridData);
		btnSave.addSelectionListener(new SelectionAdapter() {
	   		// Add a record and refresh the view
			public void widgetSelected(SelectionEvent e) {
				MapperBO record = new MapperBO();
				record.setOpVariable(txtVariableName.getText());
				record.setExpression(txtExpression.getText());
				txtVariableName.setText("");
				txtExpression.setText("");
				recordList.addRecord(record);
				tableViewer.refresh();
			}
		});
		
		Composite comp3 = new Composite(shell, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 3;
		layout.makeColumnsEqualWidth = true;
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.heightHint = 40;
		comp3.setLayout(layout);
		comp3.setLayoutData(data);

		Button btnOk = new Button(comp3, SWT.PUSH | SWT.CENTER);
		btnOk.setText("Ok");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_END);
		gridData.widthHint = 80;
		btnOk.setLayoutData(gridData);
		btnOk.addSelectionListener(new SelectionAdapter() {
	   		// Add a record and refresh the view
			public void widgetSelected(SelectionEvent e) {
				//recordList.addRecord(table.getSelectionIndex());
			}
		});
		
		Button btnClose = new Button(comp3, SWT.PUSH | SWT.CENTER);
		btnClose.setText("Cancel");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		btnClose.setLayoutData(gridData);
		
	}
	
	public static void main(String[] args) {
		
		
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("HPCC Expression Builder");
		shell.setSize(800, 700); //Sets the size of the main window
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		shell.setLayout(layout);
		shell.setLayoutData(data);
		
		//Create a DataSet
		String[] dataSetList = {"FirstName", "LastName", "Address", "City", "State", "Zip", "Telephone Number"};
		final MainMapper objMainMapper = new MainMapper(shell, dataSetList);
		
		shell.open();
		
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch())	{
				display.sleep();
			}
		}
		display.dispose();
	}
	
}



