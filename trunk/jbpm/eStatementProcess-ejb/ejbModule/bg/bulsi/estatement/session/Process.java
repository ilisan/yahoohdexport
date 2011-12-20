package bg.bulsi.estatement.session;


import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

import bg.bulsi.estatement.process.PersistentProcessManager;
import bg.bulsi.estatement.process.ProcessManager;

/**
 * @author tzvetan.stefanov@bul-si.bg
 *
 */
@Name("Process")
@Scope(ScopeType.APPLICATION)
@Startup
public class Process {
	
    /**
     * Process Manager in charge of the communication with the process and 
     * the ksession.
     */
	@Out
    private ProcessManager processManager;
	
	public ProcessManager getProcessManager() {
		return processManager;
	}

//	@PersistenceUnit(unitName="org.jbpm.persistence.jpa")
//	EntityManagerFactory entityManagerFactory;
	
	@Create
	public void onCreate(){
		Map<String, WorkItemHandler> workItemsHandlers = new HashMap<String, WorkItemHandler>();
        processManager = new PersistentProcessManager(createKBase(), createEnvironment(), workItemsHandlers, "eStatement-scriptId");

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

    /**
     * Creates the Drools Environment used to configure Persistence
     * @return 
     */
    public Environment createEnvironment() {
        //Creates a new EntityManagerFactory
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa");

        //Creates the Environment and sets its attributes
        Environment env = KnowledgeBaseFactory.newEnvironment();
        env.set(EnvironmentName.ENTITY_MANAGER_FACTORY,
                entityManagerFactory);
//        env.set(EnvironmentName.TRANSACTION_MANAGER,
//                TransactionManagerServices.getTransactionManager());

        return env;
    }
}
