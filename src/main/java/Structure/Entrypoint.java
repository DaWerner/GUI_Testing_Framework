package Structure;

import Utilities.DataExchange;
import Utilities.Report;

public class Entrypoint {

	public static void main(String[] args) {
		Report.exitCode =0;
		DataExchange hub = new DataExchange(args);
		hub.endTest();
	}
	
}
