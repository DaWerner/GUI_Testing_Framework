package Utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import com.google.gson.JsonParser;

import Utilities.CurrentContext;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.events.EventFiringWebDriver;

//import com.gargoylesoftware.htmlunit.javascript.host.file.File;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Central class to store any information that needs to be exchanged between
 * different parts of the application
 */
public class DataExchange {
	private WebDriver driver;
	private String CurrentTestCase, LinkToPublished, BackendDomain, Header;

	private HashMap<String, HashMap<String, String>> Parameters;
	private HashMap<String, Document> HTMLTrees;
	
	private Boolean Screenshots, LangCheck;
	private CLIParser parse;
	private List<String> Reports, ScreenshotsTaken, getArguments, frameSources, TestcasesDone;
	private List<File> files,Images;
	private List<TextElement> Texts;
	private Set<String> BrowserLogs, Error404s;
	private Long start;
	private int ErrorCount, testVersion;
	private CurrentContext Context;
	private String[] AccData;

	/**
	 * Constructor function Creates a new instance of DataExchange NOTE: WebDriver
	 * needs to be explicitly initialized by calling either initializeDriver or
	 * renewDrivers
	 * 
	 * @param Args = passed Arguments for initialization
	 */
	public DataExchange(String[] Args) {
		parse = new CLIParser(Args);
		this.testVersion = 0;
		this.ErrorCount = 0;
		this.BackendDomain = "http://tatooine:5000";
		this.start = System.currentTimeMillis();
		this.LinkToPublished = "NotYetPublished";
		this.HTMLTrees = new HashMap<String, Document>();
		this.Parameters = new HashMap<>();
		this.Images = new ArrayList<>();
		this.CurrentTestCase = "Login";
		this.ScreenshotsTaken = new ArrayList();
		this.Error404s = new HashSet<>();
		Texts = new ArrayList<>();
		this.Reports = new ArrayList<>();
		BrowserLogs = new HashSet<>();
		this.Context = new CurrentContext(this);
		this.TestcasesDone = new ArrayList<>();
		this.Header = getReportHeader();
		new StateServer(this).start();
	}
	
	/**
	 * Method to extract the base URL from valid URLs
	 * @param URL
	 * @return URL containing the Protocol declaration but no path data
	 */
	public String getBaseURL(String URL) {
		return URL.substring(0, UtilityFunctions.nthIndexOf(URL, "/", 3));
	}

	private String getReportHeader() {
		String Start = "{\"testServer\": \"" + this.parse.Server + "\"," + " \"testDate\": \"" + UtilityFunctions.getDate(System.currentTimeMillis(), "yyyy-MM-dd'T'hh:mm:ss") + "\","
				+ " \"testLanguage\": \"" + this.parse.Language + "\"," + " \"takesScreenshots\": " + this.parse.Screenshots + ","
				+ " \"browser\": \"" + this.parse.Browser + "\"," + " \"reseller\": \"" +  "\","
				+ " \"productSetup\": \"" + "\"," + " \"runsOnAccount\": \"" + this.parse.Account + "\","
				+ " \"testobjectsRun\":[";
		return Start;
	}

	public String getHosting() {
		return AccData[4];
	}
	
	public void addScreenshot(File F) {
		this.Images.add(F);
	}

	public List<File> getImages() {
		return this.Images;
	}

	public List<String> getFinishedTestcases(){
		return this.TestcasesDone;
	}
	
	public void addDocument(Document Doc, String Name) {
		this.HTMLTrees.put(Name, Doc);
	}

	public Document getDocument(String Name) {
		return this.HTMLTrees.get(Name);
	}

	public int getErrorCount() {
		return this.ErrorCount;
	}

	public void addScreenshotName(String S) {
		this.ScreenshotsTaken.add(S);
	}

	public Boolean getLangCheck() {
		return this.LangCheck;
	}

	public Boolean isScreenshotTaken(String S) {
		return this.ScreenshotsTaken.contains(S);
	}

	public void addFrameSource(String fS) {
		this.frameSources.add(fS);
	}
	
	public String finalizeReport() {
		String FinalRep = this.Header;
		for (String Testobject : this.Reports) {
			FinalRep += Testobject + ",";
		}
		int indexOfLastComma = FinalRep.lastIndexOf(",");
		FinalRep = FinalRep.substring(0, indexOfLastComma)
				+ FinalRep.substring(indexOfLastComma + 1, FinalRep.length());
		FinalRep += "]," + "\"linkToPublished\": \"" + this.LinkToPublished + "\"" + "}";
		return FinalRep;
	}



	public void setLinkToPublish(String Link) {
		this.LinkToPublished = Link;
	}

	public void addReport(String Rep) {
		this.Reports.add(Rep);
	}

	public String getPublishedURL() {
		return this.LinkToPublished;
	}

	/**
	 * Method to add a Map of Configuration Parameters for use in later tests
	 * (Sanitizes passed value)
	 * 
	 * @param Para
	 *            List of Configuration Parameters defined during testsuite
	 * @param Name
	 *            Test name that should be used to retrieve the Parameters later on.
	 */
	public void addParameter(HashMap<String, String> Para, String Name) {
		HashMap<String, String> Sanitized = new HashMap<>();
		for (String Key : Para.keySet()) {
			Sanitized.put(Key.replaceAll("\\.", "_"), StringEscapeUtils.escapeJson(Para.get(Key)));
		}
		this.Parameters.put(Name, Sanitized);
	}

	/**
	 * Method to retrieve stored parameters Return Empty map if no corresponding
	 * Parameters exist
	 * 
	 * @param Name
	 *            The Value initially passed to addParameter
	 * @return Map of Configuration Parameters or empty map
	 */
	public HashMap<String, String> getParameters(String Name) {
		HashMap<String, String> ParamsList = (this.Parameters.get(Name) != null) ? this.Parameters.get(Name)
				: new HashMap<>();
		HashMap<String, String> ToRet = new HashMap<>();
		for (String Key : ParamsList.keySet()) {
			ToRet.put(Key, StringEscapeUtils.unescapeJava(ParamsList.get(Key)));
		}
		return ToRet;
	}

	/**
	 * Converts all stored Parameters into a JSON confirm string to send out within
	 * the Test Report
	 * 
	 * @param TestId
	 *            MongoDB ID of Test
	 * @return JSON string containing the configuration Parameters
	 */
	public String exportParameters(String TestId) {
		Gson Jsonfy = new GsonBuilder().create();
		JsonObject ToRet = new JsonObject();
		ToRet.add("params", new Gson().toJsonTree(Parameters));
		ToRet.addProperty("testID", TestId);
		return Jsonfy.toJson(ToRet);
	}

	/**
	 * Gives back parameter to define if Screenshots of a test should be taken
	 * IMPORTANT If no screenshots get taken the test will also not look up Text
	 * Nodes speeds up test significantly but also lowers data gathered
	 * 
	 * @return Boolean
	 */
	public Boolean getScreenshots() {
		return this.Screenshots;
	}

	/**
	 * Gives back configured Selenium Hub Defaults to 127.0.0.1:4444 if not passed
	 * on start
	 * 
	 * @return String URL for Selenium Hub
	 */
	public String getHubURL() {
		return parse.HubURL;
	}


	/**
	 * Returns Language configured Defaults to en_US if not passed on start
	 * 
	 * @return String Language to use in test
	 */
	public String getLanguage() {
		return this.parse.Language;
	}

	/**
	 * Returns Account to run test on Has no default, must be set to start a test
	 * 
	 * @return String Account name
	 */
	public String getAccount() {
		return parse.Account;
	}

	
	public String getFiles() {
		try {
			HttpURLConnection Conn = (HttpURLConnection) new URL(BackendDomain +"/testconfig/getfiles").openConnection();
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(Conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			int rand = new Random().nextInt(response.toString().split(",").length-1);
			int count = 0;
			for(String val : response.toString().split(",")) {
				if(count==rand)
					return downloadFile(val);
				count++;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getFiles(String Dateiart) {
		try {
			HttpURLConnection Conn = (HttpURLConnection) new URL(BackendDomain +"/testconfig/getfiles").openConnection();
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(Conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			for(String val : response.toString().split(",")) {
				if(val.contains(Dateiart))
					return downloadFile(val);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getFiles(int sizeClass) {
		String Size = "";
		if(sizeClass == 0) Size = "small";
		else if(sizeClass == 1) Size = "medium";
		else if(sizeClass == 2) Size = "large";
		try {
			HttpURLConnection Conn = (HttpURLConnection) new URL(BackendDomain+"/testconfig/getfiles?filesize="+ Size).openConnection();
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(Conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			int rand = new Random().nextInt(response.toString().split(",").length-1);
			int count = 0;

			for(String val : response.toString().split(",")) {
				if(count==rand)
					return downloadFile(val);
				count++;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private String downloadFile(String File) throws IOException {
		String ToRet = "";
		if(!new File("./TestfuerFiles").exists()) {
			new File("./TestfuerFiles").mkdir();
		}
		String SaveDir ="./TestfuerFiles/";
		URL url = new URL("http://tatooine/TestFiles/"+File);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();
 
        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
 
            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = SaveDir + File;
            File f = new File(saveFilePath);
            f.createNewFile();
            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(f);
 
            int bytesRead = -1;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            ToRet = f.getCanonicalPath();
            outputStream.close();
            inputStream.close();
        }
        httpConn.disconnect();
        return ToRet;
    }
	

	/**
	 * Returns Mail address to send results to Defaults to qauser@cm4all.com if not
	 * set otherwise on start
	 * 
	 * @return String Email configured
	 */
	public String getMail() {
		return parse.MailAddress;
	}

	/**
	 * Returns Login call to server Has no Default, needs to be passed on start
	 * 
	 * @return
	 */
	public String getServer() {
		return parse.Server;
	}

	/**
	 * Returns in CurrentContext instance for the Session of this DataExchange
	 * Initialized on construction, no Default
	 * 
	 * @return CurrentContext
	 */
	public CurrentContext getContext() {
		return this.Context;
	}

	/**
	 * Returns the Base Url of the Server the current test is running on Build from
	 * this.Server, doesn't need to be passed down
	 * 
	 * @return Base URL of Server with trailing "/"
	 */
	public String getServerName() {
		return getBaseURL(parse.Server);
	}


	/**
	 * Return the Testcase Arguments passed to test Defaults to empty, if nothing is
	 * passed on start up it will start, setup the account and close
	 * 
	 * @return
	 */
	public ArrayList<String> getArguments() {
		return (ArrayList<String>) this.parse.Arguments;
	}


	/**
	 * Returns Translation for a given String if it exists within the PO files
	 * hosted on dev.intern.cm-ag/ If no Language is specified or the specified
	 * Language is en the original String gets returned so no need to define
	 * specific translation cases
	 * 
	 * @param ToTranslate
	 *            = String that needs to be translated
	 * @return Translation in Language defined in this.Language
	 */
	public String getTranslation(String ToTranslate) {
		if (this.parse.Language.equals("en"))
			return ToTranslate;
		try {
			String Query = "http://tatooine/Languages/getPOEntryExplicit.php?Lang=" + this.getLanguage() + "&Val="
					+ URLEncoder.encode(ToTranslate, "utf-8");
			HttpURLConnection Conn = (HttpURLConnection) new URL(Query).openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(Conn.getInputStream()));
			String Res = "";
			String Line = "";
			while ((Line = br.readLine()) != null) {
				Res += Line;
			}
			return URLDecoder.decode(Res, "utf-8");
		} catch (IOException e) {
		}
		return ToTranslate;

	}

	/**
	 * Adds Text node to this.Texts to later on be used for Language Checking
	 * 
	 * @param Text
	 */
	public void addText(TextElement Text) {
		this.Texts.add(Text);
	}


	/**
	 * Exports JSONfied Texts gathered during Testing to Textfile
	 * "TextNode_[LanguageID].txt" Overwrites last occurence of same languageData
	 * file, if existing, to avoid clutter
	 */
	public String exportText(String ID) {
		String MyText = "{\"id\":\"" + ID + "\", \"Texts\":[";
		Iterator<TextElement> UniqueTexts = new HashSet<>(flattenTexts(Texts)).iterator();
		while (UniqueTexts.hasNext()) {
			String ToAdd = UniqueTexts.next().toString();
			ToAdd = ToAdd;
			
			if (UniqueTexts.hasNext()) {
				MyText += ToAdd + ",";
			} else {
				MyText += ToAdd;
			}
		}

		MyText += "]}";
		return MyText;

	}
	
	private List<TextElement> flattenTexts(List<TextElement> Texts){
		List<TextElement> ToRet = new ArrayList<>();
		List<String> Seen = new ArrayList<>();
		for(TextElement Text : Texts) {
			if(!Seen.contains(Text.getText())) {
				ToRet.add(Text);
				Seen.add(Text.getText());
			}
		}
		return ToRet;
	}

	/**
	 * Setter for currently active Widget, doesn't really have a functional use just
	 * for logging and monitoring purposes
	 * 
	 * @param currentWidget
	 *            = Feature currently under test
	 */
	public void setCurrentTestCase(String current) {
		this.TestcasesDone.add(CurrentTestCase);
		CurrentTestCase = current;
		this.driver.getTitle(); // DO NEVER EVER UNDER ANY CIRCUMSTANCES REMOVE,
								// necessary to keep the Editor Browser alive during tests on the published
								// page.
		System.out.println("<Info>Testing: " + current + "</Info>");
	}

	/**
	 * Returns an Instance of the currently active Webdriver
	 * 
	 * @return Selenium WebDriver currently used for all Instances supervised by
	 *         this instance of DataExchange
	 */
	public WebDriver getDriver() {
		try {
			this.driver.getWindowHandle();
		} catch (UnreachableBrowserException e) {
		}
		return this.driver;
	}

	/**
	 * Returns Browser used for the current Test Session Defaults to Firefox if no
	 * other Argument was passed on start
	 * 
	 * @return
	 */
	public String getBrowser() {
		return this.parse.Browser;
	}

	/**
	 * Sets a new driver for all Object supervised by this instance of Dataexchange
	 * Should only be used at start up or if the change of browsers (with loss of
	 * session data e.g. cookies) is explicitly wanted. Use initializeDriver to
	 * create a new Webdriver instance if you only need a new browser window
	 * 
	 */
	public void renewDrivers() {
		this.driver = initializeDriver();
		this.Context.setDriver(this.driver);
	}

	/**
	 * @return Name of Widget currently under test
	 */
	public String getCurrentTestcase() {
		return CurrentTestCase;
	}


	public String getBrowserlog(String TestID) {
		String Header = "{\"logs\": [";
		String Entries = "";
		for (String Entry : BrowserLogs) {

			Entries += Entry + ",";
		}
		// this.Error404s.forEach(x->{System.out.println(x);});
		if (Entries.lastIndexOf(",") > 0)
			Entries = Entries.substring(0, Entries.lastIndexOf(","));
		return Header + Entries + "], \"testId\":\"" + TestID + "\"}";
	}

	private void add404(String Entry) {
		String Res = Entry.split(" - ")[0];
		String Message = Entry.split(" - ")[1];
		Error404s.add("{\"Resource\" :" + Res + "\", \"Message\": \"" + Message + "\" }");
	}

	public void addBrowserLog() {
		try {
			LogEntries Logs = this.driver.manage().logs().get(LogType.BROWSER);
			for (LogEntry Log : Logs) {
				if (Log.getMessage().contains("responded with a status of ")) {
					add404(Log.getMessage());
				}
				String PseudoJSON = "{\"Level\": \"" + Log.getLevel() + "\", \"Message\":\""
						+ StringEscapeUtils.escapeJson(Log.getMessage()) + "\", \"Testobject\":\""+getCurrentTestcase()+"\" }";
				try {
					new JsonParser().parse(PseudoJSON);
					this.BrowserLogs.add(PseudoJSON);
				} catch (Exception e) {
					System.out.println("Unparsable Browserlog: " + PseudoJSON);
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Initializes a new WebDriver Instance for the profile defined in this instance
	 * of Dataexchange. Doesn't become the new default WebDriver for instances
	 * monitored by this instance of DataExchange, if this is wanted use
	 * this.renewDrivers() instead.
	 * 
	 * @return WebDriver instance
	 */
	public WebDriver initializeDriver() {
		return this.initRemote();
		// return this.initLocal();

	}

	private RemoteWebDriver initRemote() {
		int tryNewDriver = 0;
		String Browser = parse.Browser;
		String HubURL = parse.HubURL;
		DesiredCapabilities Capability = null;
		Dimension ScreenSize = new Dimension(1300, 2000 * 2);
		while (tryNewDriver < 10) {
			if (Browser.equals("Chrome")) {
				System.setProperty("webdriver.chrome.verboseLogging", "true");
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--start-maximized");
				options.addArguments("--v=0");
				Capability = DesiredCapabilities.chrome();
				LoggingPreferences lPrefs = new LoggingPreferences();
				lPrefs.enable(LogType.BROWSER, Level.ALL);
				lPrefs.enable(LogType.PERFORMANCE, Level.ALL);

				Map<String, Object> perfLogPrefs = new HashMap<String, Object>();
				// perfLogPrefs.put("traceCategories", "console-api");
				Capability.setCapability(CapabilityType.LOGGING_PREFS, lPrefs);
				options.setExperimentalOption("perfLoggingPrefs", perfLogPrefs);
				Capability.setCapability(ChromeOptions.CAPABILITY, options);
				// Capability.setCapability(CapabilityType.PROXY,
				// ClientUtil.createSeleniumProxy(BM_Proxy));
				Capability.setBrowserName("chrome");
				RemoteWebDriver driver;

				try {
					driver = new RemoteWebDriver(new URL(HubURL), Capability);
					return (driver);
				} catch (Exception e) {
					tryNewDriver++;
					e.printStackTrace();
				}
			} else if (Browser.equals("Firefox")) {
				FirefoxProfile firefoxProfile = new FirefoxProfile();
				firefoxProfile.setPreference("browser.download.folderList", 2);
				firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);
				firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk",
						"application/xml,text/plain,text/xml,image/jpeg,image/jpg,image/png,image/bmp,image/gif");
				Capability = DesiredCapabilities.firefox();
				Capability.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
				RemoteWebDriver driver;
				try {
					driver = new RemoteWebDriver(new URL(HubURL), Capability);
					driver.manage().window().setSize(ScreenSize);
					return (driver);
				} catch (Exception e) {
					tryNewDriver++;
					e.printStackTrace();
				}
			} else if (Browser.equals("IE")) {
				Capability = new DesiredCapabilities();
				Capability.setBrowserName(Browser);
				RemoteWebDriver driver;
				try {
					driver = new RemoteWebDriver(new URL(HubURL), DesiredCapabilities.internetExplorer());
					driver.manage().window().setSize(ScreenSize);
					return (driver);
				} catch (Exception e) {
					tryNewDriver++;
				}
			} else if (Browser.equals("EDGE")) {
				Capability = new DesiredCapabilities();
				Capability.setBrowserName(Browser);
				RemoteWebDriver driver = null;
				try {
					driver = new EdgeDriver();
					driver.manage().window().setSize(ScreenSize);
					return (driver);
				} catch (Exception e) {
					tryNewDriver++;
					e.printStackTrace();
				}
			} else {
				Capability = new DesiredCapabilities();
				Capability.setBrowserName(Browser);
				RemoteWebDriver driver;
				try {
					driver = new RemoteWebDriver(new URL(HubURL), DesiredCapabilities.firefox());
					driver.manage().window().setSize(ScreenSize);
					return driver;
				} catch (Exception e) {
					tryNewDriver++;
					e.printStackTrace();
				}
			}
		}
		return (RemoteWebDriver) driver;
	}

	/**
	 * @return Returns Data for the Account if it needs to initialized.
	 *         <ul style="list-style: none">
	 *         <li>[0] contains ResellerID defaults to "cm4all"</li>
	 *         <li>[1] contains Locale defaults to "en_US"</li>
	 *         <li>[2] contains Timezone defaults to "Africa/Abidjan"</li>
	 *         <li>[3] contins ProductSetup ID defaults to "FVALL"</li>
	 *         <li>[4] contains desired hosting Server defaults to
	 *         "phphosting.intern.cm-ag/"</li>
	 *         </ul>
	 */
	public String[] getAccountData() {
		return this.AccData;
	}


	public void logResultLocally() {
		System.out.println("Results: \n\n");
		String date = UtilityFunctions.getDate(System.currentTimeMillis(), "yyyy-MM-dd'T'hh-mm-ss");
		String testResults= finalizeReport();
		String bLogs = getBrowserlog(date);
		String Texts = (parse.LangCheck)? exportText(date): "";
		String toLog = "Functional Results:\n\n" +testResults +
				       "\n\nLogged Info:\n\n" + bLogs +
				       "\n\nText Nodes:\n\n" + Texts;
		System.out.println(toLog);

		
		File newLog = new File(String.format("/var/log/testresults/%s.log","Testresult_from_"+ date));
		try {
			newLog.createNewFile();
			BufferedWriter bw =new BufferedWriter(new FileWriter(newLog));
			bw.write(toLog);
			bw.close();
		} catch (IOException e) {
			System.err.println("Couldn't log Result");
			e.printStackTrace();
		}
	}

	/**
	 * Method to finalize results and send to tatooine for further processing
	 */
	public String sendData() {
		if(this.parse.BackendURL.equals("none")) {
			logResultLocally();
			return "Logged locally at /var/log/testresults";
		}
		
		
		String Result = "";
		try {
			Result = finalizeReport().replaceAll("\\\\", "[backslash]");
			String urlParams = "Team=" + this.parse.Team + "&TestRes=" + URLEncoder.encode(Result, "UTF-8");
			String TatooineUrl = BackendDomain +"/testresults/postresult";
			HttpURLConnection Conn = (HttpURLConnection) new URL(TatooineUrl).openConnection();
			Conn.setRequestMethod("POST");
			Conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			Conn.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(Conn.getOutputStream());
			wr.writeBytes(urlParams);
			wr.flush();
			wr.close();

			int responseCode = Conn.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(Conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine.toString());
				response.append(inputLine);
			}
			in.close();
			String BrowserLogsJSON = this.getBrowserlog(response.toString());
			if (this.LangCheck) {
				sendTextNodes(response.toString());
			}
			System.out.println(BrowserLogsJSON);
			this.sendBrowserLogs(BrowserLogsJSON);

			System.out.println("Report available under: http://tatooine.intern.cm-ag/report.html?id="
					+ response.toString() + "&team=" + this.parse.Team);
			if (this.getScreenshots()) {
				uploadScreenshots(response.toString());
			}
			this.uploadScreenshots(response.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Result;
	}

	private String sendTextNodes(String TestId) {
		if (!this.LangCheck)
			return TestId;
		try {
			String Texts = this.exportText(TestId);
			String urlParams = "lang=" + this.parse.Language + "&result=" + URLEncoder.encode(Texts, "UTF-8");
			String TatooineUrl = BackendDomain +"/lang/postdata";
			HttpURLConnection Conn = (HttpURLConnection) new URL(TatooineUrl).openConnection();
			Conn.setRequestMethod("POST");
			Conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			Conn.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(Conn.getOutputStream());
			wr.writeBytes(urlParams);
			wr.flush();
			wr.close();

			int responseCode = Conn.getResponseCode();
			System.out.println(responseCode);
			BufferedReader in = new BufferedReader(new InputStreamReader(Conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return TestId;
	}

	private String sendBrowserLogs(String TestId) {
		try {
			String urlParams = "logs=" + URLEncoder.encode(TestId, "UTF-8");
			String TatooineUrl = BackendDomain +"/testresults/logs/add";
			HttpURLConnection Conn = (HttpURLConnection) new URL(TatooineUrl).openConnection();
			Conn.setRequestMethod("POST");
			Conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			Conn.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(Conn.getOutputStream());
			wr.writeBytes(urlParams);
			wr.flush();
			wr.close();

			int responseCode = Conn.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(Conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return TestId;
	}

	public int listSize() {
		return files.size();
	}

	private void uploadScreenshots(String id) {
		for (File F : this.Images) {
			try {
				UtilityFunctions.upload(new URL(BackendDomain +"/testresults/screenshots/add"), F, id);
				F.deleteOnExit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void endTest() {
		Long end = System.currentTimeMillis();
		Long fin = (end - start) / 1000;
		String FinalTime = fin + " Seconds";
		if (fin / 60 > 1) {
			Long Seconds = fin % 60;
			Long Minutes = Math.floorDiv(fin, 60);
			FinalTime = Minutes + " Minutes and " + Seconds + " Seconds";
		}
		if (driver != null && !driver.toString().contains("(null)")) {
			this.driver.quit();
		}
		System.out.println("\n\n\n\n");
		System.out.println("Test started at: " + UtilityFunctions.getDate(start, "hh:mm:ss"));
		System.out.println("Test fninished at: " + UtilityFunctions.getDate(end, "hh:mm:ss"));
		System.out.println("Total Runtime: " + FinalTime);
		System.out.println("\n\n\n\n");
		System.out.println("Exit Code: " + Report.exitCode);
		String Result = this.sendData();
		System.out.println("Test Result : " + Result);
		System.exit(Report.exitCode);
	}

}
