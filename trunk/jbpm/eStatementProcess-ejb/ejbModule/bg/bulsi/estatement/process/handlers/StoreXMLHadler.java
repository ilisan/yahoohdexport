package bg.bulsi.estatement.process.handlers;

import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import bg.bulsi.estatement.register.IRegPortal;

/**
 * @author tzvetan.stefanov@bul-si.bg
 *
 */
@Name("StoreXMLHadler")
public class StoreXMLHadler implements WorkItemHandler{
	
	@In(value = "DummyRegPortal")
	private IRegPortal regPortal;

	public void abortWorkItem(WorkItem item, WorkItemManager mgr) {
		// TODO Auto-generated method stub
		
	}

	public void executeWorkItem(WorkItem item, WorkItemManager mgr) {
		item.getParameter("");
		
	}

}
