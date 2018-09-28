package Utilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

import org.apache.commons.text.StringEscapeUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Report {
	public static int exitCode;
	private TreeMap<String, ReportItem> Testcases;
	private String TestObject;
	private String Report;
	Boolean takeNewOnes;
	private long StartTime;
	private int RunOrder;

	/**
	 * Constructor Method for new Test Report
	 * 
	 * @param TO
	 *            Name of Testobject under test
	 */
	public Report(String TO) {
		this.RunOrder = 0;
		this.takeNewOnes = true;
		this.TestObject = TO;
		this.Testcases = new TreeMap<>();
		this.StartTime = System.currentTimeMillis();
	}

	/**
	 * Method to initialize a new Testcase
	 * 
	 * @param TC
	 */
	public void addTestcase(String TC, String Desc) {
		this.Testcases.put(TC, new ReportItem(TestObject, TC, Desc, this.RunOrder));
		this.RunOrder++;
	}

	/**
	 * Method to add a new Result
	 * 
	 * @param TC
	 *            Name of the Testcase, must match a Testcase created via
	 *            addTestcase()
	 * @param Test
	 *            Name of the Test
	 * @param Result
	 *            Boolean value returned by test (null for exits because of non
	 *            present Elements)
	 * @param painValue TODO
	 */
	public void addResult(String TC, String Test, Boolean Result, int painValue) {
		if (!takeNewOnes)
			Result = false;
		if(Testcases.get(TC) == null)
			throw new IllegalStateException("Testcase " + TC + " has not been declared\nDeclare a new Testcase by using addTestcase");
		this.Testcases.get(TC).addResult(Test, Result, painValue);
	}

	
	/**
	 * Method to add a new Result
	 * 
	 * @param TC
	 *            Name of the Testcase, must match a Testcase created via
	 *            addTestcase()
	 * @param Test
	 *            Name of the Test
	 * @param Result
	 *            Boolean value returned by test (null for exits because of non
	 *            present Elements)
	 */
	public void addResult(String TC, String Test, Boolean Result) {
		if (!takeNewOnes)
			Result = false;
		if(Testcases.get(TC) == null)
			throw new IllegalStateException("Testcase " + TC + " has not been declared\nDeclare a new Testcase by using addTestcase");
		this.Testcases.get(TC).addResult(Test, Result, 0);
	}

	public void setAllFalse() {
		this.takeNewOnes = false;
		for (String Key : Testcases.keySet()) {
			Testcases.get(Key).addResult("couldBeExecuted", false, 1);
		}
	}

	/**
	 * Method to jsonfy Report
	 * 
	 * @param Params
	 *            TODO
	 * @return json representation of the report
	 */
	public String finalizeReport(HashMap<String, String> Params) {
		HashMap<String, String> Sanitized = new HashMap<>();
		if (Params.isEmpty()) {
			for (String Key : Params.keySet()) {
				if(Params.get(Key).equals("")) Params.put(Key, "notUsed");
				String Value = StringEscapeUtils.escapeEcmaScript(Params.get(Key).replaceAll("\\\\", ""));
				Sanitized.put(Key, Value);
			}
		}else { Sanitized = Params;}
		long EndTime = System.currentTimeMillis();
		Gson gson = new GsonBuilder().serializeNulls().create();
		long Total = (EndTime - StartTime) / 1000;
		int OverallResult = (Testcases.size() == 0) ? 1 : 0;
		String Start = "{\"testobject\": \"" + this.TestObject + "\"," + "\"runTime\": " + Total + ", "
				+ "\"testcases\": [";
		for (String TC : this.Testcases.keySet()) {
			ReportItem Testcase = Testcases.get(TC);
			OverallResult += Testcase.finalizeReport();
			try {
				Start += Testcase.getResult() + ",";
			} catch (Exception e) {

			}
		}
		int indexOfLastComma = Start.lastIndexOf(",");
		Start = Start.substring(0, indexOfLastComma) + Start.substring(indexOfLastComma + 1, Start.length());
		Start += "],";
		Start += "\"params\": " + gson.toJson(Sanitized) + ",";
		Start += "\"testObjectResult\": \"" + ((OverallResult == 0) ? "succeeded" : "failed") + "\"}";
		return Start;
	}

	/**
	 * Method to jsonfy Report with testresult other than "succeeded" or "failed"
	 * 
	 * @param nonStandardResult
	 *            String containing the Testresult to display
	 * @return json representation of the report
	 */
	public String finalizeReport(String nonStandardResult, HashMap<String, String> Params) {
		HashMap<String, String> Sanitized = new HashMap<>();
		if (Params.isEmpty()) {
			for (String Key : Params.keySet()) {
				if(Params.get(Key).equals("")) Params.put(Key, "notUsed");
				String Value = StringEscapeUtils.escapeEcmaScript(Params.get(Key).replaceAll("\\\\", ""));
				Sanitized.put(Key, Value);
			}
		}else { Sanitized = Params;}
		long EndTime = System.currentTimeMillis();
		long Total = (EndTime - StartTime) / 1000;
		Gson gson = new GsonBuilder().serializeNulls().create();
		int OverallResult = 0;
		String Start = "{\"testobject\": \"" + this.TestObject + "\"," + "\"runTime\": " + Total + ", "
				+ "\"testcases\": [";
		for (String TC : this.Testcases.keySet()) {
			ReportItem Testcase = Testcases.get(TC);
			OverallResult += Testcase.finalizeReport();
			try {
				Start += Testcase.getResult() + ",";
			} catch (Exception e) {

			}
		}
		exitCode += OverallResult;
		int indexOfLastComma = Start.lastIndexOf(",");
		Start = Start.substring(0, indexOfLastComma) + Start.substring(indexOfLastComma + 1, Start.length());
		Start += "],";
		Start += "\"params\": " + gson.toJson(Sanitized) + ",";
		Start += "\"testObjectResult\": " + nonStandardResult + "}";
		return Start;
	}

	public void setStartTime(long Start) {
		this.StartTime = Start;

	}

}

class ReportItem {
	private String TestObject;
	private String Testcase;
	private String Description;
	private TreeMap<String, Boolean> Steps;
	private TreeMap<String, Integer> PainVal;
	private String TestcaseRes;
	private String JSON;
	private int RunOrder;
	Boolean isFinal;

	/**
	 * Constructor function for a new ReportItem
	 * 
	 * @param TO
	 *            Testobject this Testcase is associated with
	 * @param TC
	 *            Testcase name
	 * @param Desc
	 *            Short description of the Testcase
	 * @param Order
	 *            order in which the test is run (ascending number)
	 */
	public ReportItem(String TO, String TC, String Desc, int Order) {
		this.TestObject = TO;
		this.Testcase = TC;
		this.RunOrder = Order;
		this.Description = Desc;
		this.Steps = new TreeMap<>();
		this.PainVal = new TreeMap<>();
	}

	/**
	 * Method to add a new result
	 * 
	 * @param Step
	 *            Name of the specific Test
	 * @param Result
	 *            Boolean value containing the result of the test
	 * @param painValue Numeric value ranking the resulting user pain resulting if this test fails
	 */
	void addResult(String Step, Boolean Result, int painValue) {
		this.Steps.put(Step, Result);
		this.PainVal.put(Step, painValue);
	}

	/**
	 * Method to jsonfy the testcase report. Must be called before getResult can be
	 * called
	 * 
	 * @return Integer representing the sum of faillevels, 0 represents a successful
	 *         test everything else is a fail
	 */
	int finalizeReport() {
		this.isFinal = true;
		int Success = 0;
		if (Steps.size() == 0) {
			Success = 1;
			Steps.put("allTestCasesRan", false);
		} else {
			Steps.put("allTestCasesRan", true);
		}
		for (String Key : Steps.keySet()) {
			if (Steps.get(Key) == null) {
				Success = 2;
			} else if (Steps.get(Key) == false) {
				Success = 1;
			}

		}
		this.TestcaseRes = interpretFailLevel(Success);
		Gson gson = new GsonBuilder().serializeNulls().create();
		JSON = "{\"testcase\": \"" + this.Testcase + "\", \"testCaseOrder\": " + this.RunOrder
				+ ", \"testResultVerbose\": " + this.TestcaseRes + ", \"testResult\": " + Success
				+ ", \"testDescription\": \"" + this.Description + "\"" + ", \"testsRun\": " + "[";
		JSON += gson.toJson(Steps) + "]}";
		return Success;
	}

	/**
	 * Method to get the jsonfied result. finalizeResult() must be called beforehand
	 * 
	 * @return jsonfied String containing testcase info
	 * @throws Exception
	 *             thrown if the report has not been finalized
	 */
	String getResult() throws Exception {
		if (!isFinal)
			throw new Exception("Report needs to be finalized");
		return this.JSON;
	}

	/**
	 * Method to interpret the faillevel to add a verbose String
	 * 
	 * @param level
	 *            Integer between 0 and 2
	 * @return text representation of the fail level
	 */
	private String interpretFailLevel(int level) {
		if (level == 0) {
			return "\"succeeded\"";
		} else if (level == 1) {
			return "\"failedButElementExists\"";
		} else {
			return "\"failedElementCouldNotBeFound\"";
		}
	}

}
