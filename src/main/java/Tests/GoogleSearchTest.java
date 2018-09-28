package Tests;

import java.util.HashMap;

import Controls.GoogleSearchInterface;
import Structure.TestContainer;
import Utilities.DataExchange;
import Utilities.Report;
import Utilities.UtilityFunctions;

public class GoogleSearchTest implements TestContainer{
	private Report rep;
	private HashMap<String, String> parameters;
	private DataExchange hub;
	
	
	public GoogleSearchTest(String name, DataExchange hub) {
		this.hub = hub;
		this.rep = new Report(name);
		this.parameters = new HashMap<>();
		this.declareTestData();
		this.declareTestcases();
	}
	
	private void declareTestcases() {
		this.rep.addTestcase("search_for_Term", "Uses google to search for a term");
		this.rep.addTestcase("check_offered_services", "checks availability of google services");
	}
	
	private void declareTestData() {
		this.parameters.put("Search_Term", "Test");
		this.parameters.put("URL", "https://google.com");
	}
	
	private void doTest() {
		UtilityFunctions.openPage(hub, parameters.get("URL"));
		GoogleSearchInterface gSearch = new GoogleSearchInterface(hub);
		rep.addResult("search_for_Term", "write_Empty", gSearch.writeSearchTerm(""));
		rep.addResult("search_for_Term", "check_empty_search_is_ignored", !gSearch.search());
		rep.addResult("search_for_Term", "write_search_term", gSearch.writeSearchTerm(parameters.get("Search_Term")));
		rep.addResult("search_for_Term", "search_for_Term", gSearch.search());
	}

	@Override
	public void run() {
		try {
			doTest();
		} finally {
			hub.addReport(rep.finalizeReport(parameters));
		}
		
	}

	@Override
	public Report getReport() {
		return rep;
	}

	@Override
	public HashMap<String, String> getConfigParams() {
		return parameters;
	}
	
	
}
