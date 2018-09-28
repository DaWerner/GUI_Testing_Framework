
package Utilities;

import java.util.HashMap;

public class TimeoutTable {
	private HashMap<String, Integer> Timeouts;
	
	public TimeoutTable() {
		this.Timeouts = new HashMap<>();
		fillTimeouts();
	}
	
	public int getTimeout(String Test) {
		if(Timeouts.get(Test) == null) return 90;
		else return Timeouts.get(Test);
	}
	
	private HashMap<String, Integer> fillTimeouts(){
		HashMap<String, Integer> Tests = new HashMap<>();
		return Tests;
	}
	

}

