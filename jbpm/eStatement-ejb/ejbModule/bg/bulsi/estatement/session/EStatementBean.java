package bg.bulsi.estatement.session;

import java.util.HashMap;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.hibernate.validator.Length;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.core.Events;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import bg.bulsi.estatement.register.IRegPortal;
import bg.bulsi.estatement.process.Process;

/**
 * @author tzvetan.stefanov@bul-si.bg
 *
 */
@Stateful
@Name("eStatement")
public class EStatementBean implements EStatement
{
    @Logger 
    private Log log;

    @In 
    private StatusMessages statusMessages;
    
    
    @In(create = true, value = "DummyRegPortal" )
    IRegPortal dummyRegPortal;
    
    @In(value = "#{Process}")
    private Process process;
    
    
    
    @Out(required = false, scope = ScopeType.BUSINESS_PROCESS)
    private String submittedDocument;

    private String value;

    public void eStatement()
    {
        // implement your business logic here
        log.info("eStatement.eStatement() action called with: #{eStatement.value}");
        submittedDocument = value;
//		if (Events.exists()) {
//			Events.instance().raiseEvent(Process.START_PROCESS_EVENT, new HashMap<String, Object>());
//		}
//        processManager.startProcess(null);
//        processManager.fireAllRules();
        process.startProcess(new HashMap<String, Object>());
        
        statusMessages.add("eStatement #{eStatement.value}");
    }

    // add additional action methods

    @Length(max = 10)
    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

	@Destroy
	@Remove
	public void destroy() {
	}

	public void clear() {
		value = null;
		
	}

}
