package org.hpccsystems.pentaho.job.eclproject;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class MapperContentProvider implements IStructuredContentProvider{

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getElements(Object arg0) {
<<<<<<< HEAD
		return ((MapperRecordList)arg0).getRecords().toArray();
=======
		return ((RecordList)arg0).getRecords().toArray();
>>>>>>> e3817dab9afa0cee261ee0d81604e8c6f414dd6a
	}

}
