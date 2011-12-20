package bg.bulsi.estatement.session;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.hibernate.validator.Length;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import bg.bulsi.estatement.process.ProcessManager;
import bg.bulsi.estatement.register.IRegPortal;

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
    
    @In
    private ProcessManager processManager;
    
    @In(create = true, value = "DummyRegPortal" )
    IRegPortal dummyRegPortal;
    
    @Out(required = false)
    private String submittedDocument;

    private String value;

    public void eStatement()
    {
        // implement your business logic here
        log.info("eStatement.eStatement() action called with: #{eStatement.value}");
        submittedDocument = value;
        processManager.startProcess(null);

//        processManager.fireAllRules();
        
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
