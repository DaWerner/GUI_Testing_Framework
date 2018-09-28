package Structure;
import java.util.HashMap;

import Utilities.Report;

public interface TestContainer extends Runnable {
	
	
	/**
	 * Method to get accumulated report Data from Testcontainer 
	 * @return Report data gathered
	 */
	public Report getReport();
	
	/**
	 * Method to retrieve a Hashmap containing Config Parameters
	 * @return Config Parameters used while configuration
	 */
	public HashMap<String, String> getConfigParams();
	
}
