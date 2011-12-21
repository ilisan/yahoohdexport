package bg.bulsi.estatement.process;

import java.util.Map;

import org.drools.runtime.Environment;

public interface Process {

	public static final String START_PROCESS_EVENT = "bg.bulsi.estatement.StartProcess";

	public abstract ProcessManager getProcessManager();

	public abstract void onCreate();

	public abstract void startProcess(Map<String, Object> parameters);

	/**
	 * Creates the Drools Environment used to configure Persistence
	 * @return 
	 */
	public abstract Environment createEnvironment();

}