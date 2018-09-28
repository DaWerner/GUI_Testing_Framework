package Structure;

import java.util.HashMap;

import Tests.GoogleSearchTest;
import Utilities.DataExchange;
import Utilities.Report;
import Utilities.TimeoutTable;
import Utilities.UtilityFunctions;

public class Entrypoint {

	public static void main(String[] args) {
		Report.exitCode =0;
		DataExchange hub = new DataExchange(args);
		hub.renewDrivers();
		executeTestsuite(hub, fillTests(hub));
		hub.endTest();
	}
	
	
	private static void executeTestsuite(DataExchange Hub,HashMap<String, TestContainer> TestToRun) {
		TimeoutTable Timeouts = new TimeoutTable();
		for(String Testobject : Hub.getArguments()) {
			if(TestToRun.get(Testobject) != null) {
				try {
					long TimeOut = Timeouts.getTimeout(Testobject);
					runTest(Hub, TestToRun.get(Testobject), TimeOut, Testobject);
					UtilityFunctions.reloadPage(Hub);
				}catch (Exception e) {
					UtilityFunctions.reloadPage(Hub);
				}
			}
		}
	}
	

	/**
	 * Method to start a Textcontainer object in it's own thread and terminate it if it hasn't finished 
	 * within the specified timeframe
	 * @param Hub Instance of Dataexchange
	 * @param TestContainer Object implementing the TestContainer interface
	 * @param Timeout timeframe after which the test should be aborted in seconds
	 */
	private static Boolean runTest(DataExchange Hub,TestContainer TestContainer, long Timeout, String TestObjectName) {
		System.out.println("<Info>Executing: " + TestObjectName + "</Info>");
		TestContainer.getReport().setStartTime(System.currentTimeMillis());
		System.out.println("<Info>Started: " + UtilityFunctions.getDate(System.currentTimeMillis(), "hh:mm:ss") + "</Info>" );
		Hub.setCurrentTestCase(TestObjectName);
		Thread TestToRun = new Thread(TestContainer);
		TestToRun.start();
		long TimedOut = System.currentTimeMillis() + Timeout*1000;
		while(System.currentTimeMillis() < TimedOut) {
			if(!TestToRun.isAlive()) {
				Hub.addParameter(TestContainer.getConfigParams(), TestObjectName);
				Hub.addBrowserLog();
				System.out.println("<Info> Finished: " + UtilityFunctions.getDate(System.currentTimeMillis(), "hh:mm:ss") + "</Info>" );
				return true;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
			}
		}
		TestToRun.stop();
		Hub.addReport(TestContainer.getReport().finalizeReport("\"Timed out after: " + Timeout+ " seconds\"", TestContainer.getConfigParams()));
		Hub.addBrowserLog();
		System.out.println("<Info> Finished due to Timeout: " + UtilityFunctions.getDate(System.currentTimeMillis(), "hh:mm:ss") + "</Info>" );
		UtilityFunctions.reloadPage(Hub);
		return false;
	}
	
	private static HashMap<String, TestContainer> fillTests(DataExchange hub){
		HashMap<String, TestContainer> toRet = new HashMap<>();
		toRet.put("Google_Search", new GoogleSearchTest("Google_Search", hub));
		return toRet;
	}
}
