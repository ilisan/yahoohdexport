package bg.bulsi.estatement.session;


import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.process.WorkItemHandler;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.log.Log;
import org.jboss.seam.transaction.Transaction;

import bg.bulsi.estatement.process.PersistentProcessManager;
import bg.bulsi.estatement.process.ProcessManager;
import bg.bulsi.estatement.process.Process;
import bitronix.tm.TransactionManagerServices;

/**
 * @author tzvetan.stefanov@bul-si.bg
 *
 */
@Name("Process")
@Scope(ScopeType.APPLICATION)
@Startup
public class ProcessImpl implements Process {
	
    @Logger 
    private Log log;

    /**
     * Process Manager in charge of the communication with the process and 
     * the ksession.
     */
	@Out
    private ProcessManager processManager;
	
	/* (non-Javadoc)
	 * @see bg.bulsi.estatement.session.Process#getProcessManager()
	 */
	public ProcessManager getProcessManager() {
		return processManager;
	}

//	@PersistenceUnit(unitName="org.jbpm.persistence.jpa")
//	EntityManagerFactory entityManagerFactory;
	
	/* (non-Javadoc)
	 * @see bg.bulsi.estatement.session.Process#onCreate()
	 */
	@Create
	public void onCreate(){
		Map<String, WorkItemHandler> workItemsHandlers = new HashMap<String, WorkItemHandler>();
        processManager = new PersistentProcessManager(createKBase(), createEnvironment(), workItemsHandlers, "eStatement-scriptId");

	}

    /* (non-Javadoc)
	 * @see bg.bulsi.estatement.session.Process#startProcess(java.util.Map)
	 */
//    @Observer(value = Process.START_PROCESS_EVENT)
	@Asynchronous
    public void startProcess(Map<String, Object> parameters) {
		
		UserTransaction utx = Transaction.instance();
		try {
			utx.begin();
			processManager.startProcess(parameters);
			utx.commit();
		} catch (NotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    /**
     * Creates a kbase containing the process definition and a set
     * of validation rules.
     * @return 
     */
    private KnowledgeBase createKBase() {
        //Creates a builder
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        //Adds resources to the builder
        kbuilder.add(new ClassPathResource("eStatement-script.bpmn"), ResourceType.BPMN2);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();

        //Checks for errors
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error.getMessage());

            }
            throw new IllegalStateException("Error building kbase!");
        }

        //Creates a new kbase and add all the packages from the builder
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

        return kbase;
    }

    /* (non-Javadoc)
	 * @see bg.bulsi.estatement.session.Process#createEnvironment()
	 */
    public Environment createEnvironment() {
        //Creates a new EntityManagerFactory
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa");

        //Creates the Environment and sets its attributes
        Environment env = KnowledgeBaseFactory.newEnvironment();
        env.set(EnvironmentName.ENTITY_MANAGER_FACTORY,
                entityManagerFactory);
        env.set(EnvironmentName.TRANSACTION_MANAGER,
                TransactionManagerServices.getTransactionManager());

        return env;
    }
}
