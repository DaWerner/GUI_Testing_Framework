package Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UtilityFunctions {
	public static void reloadPage(DataExchange Hub) {
		try {
			Hub.getDriver().get(Hub.getDriver().getCurrentUrl());
		} catch (Exception e) {
		}
	}

	public static Document getDOM(DataExchange Hub) {
		try {
			String DOM = Hub.getDriver().findElement(By.tagName("html")).getAttribute("innerHTML");
			return Jsoup.parse(DOM);
		} catch (Exception e) {
			return null;
		}
	}

	public static Boolean openPage(DataExchange Hub, String Name) {
		try {
			String PubRUL = Name;
			Hub.getDriver().get(PubRUL);
			HttpURLConnection Conn = (HttpURLConnection) new URL(PubRUL).openConnection();
			return Conn.getResponseCode() < 400;
		} catch (Exception e) {
			return false;
		}
	}

	public static String getRandomFromSet(Set<String> MySet) {
		try {
			int Rand = new Random().nextInt(MySet.size());
			return MySet.toArray(new String[MySet.size()])[Rand];
		} catch (Exception e) {
			return "";
		}
	}

	public static String generateRandomHexColor() {
		Random random = new Random();
		int nextInt = random.nextInt(256 * 256 * 256);
		String colorCode = String.format("#%06x", nextInt);
		return colorCode;
	}
	
	
	public static void highLightElement(WebElement element, WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			
		}
		js.executeScript("arguments[0].setAttribute('style','border: solid 2px white');", element);
	}

	public static void takeScreenshot(DataExchange Hub, String Name) {
		List<String> Vals = new ArrayList<>();
		Boolean TakeSS = true;
		Boolean isTaken = true;
		Name = Hub.getCurrentTestcase() + "___" + Name;
		if (Hub.isScreenshotTaken(Name) || !Hub.getScreenshots())
			return;
		else {
			try {
			if(!new File("./tmp"+Hub.getLanguage()).exists()) {
				new File("./tmp"+Hub.getLanguage()).mkdir();
			}
			File scrFile = ((TakesScreenshot) Hub.getDriver()).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File("./tmp"+Hub.getLanguage()+ "/" + Name + ".png"));
			Hub.addScreenshot(new File("./tmp"+Hub.getLanguage() +"/"+ Name + ".png"));
			Hub.addScreenshotName(Name);
			String Filename = "../Screenshots/__IDPLACEHOLDER__/"+Name;
			if(Hub.getLangCheck()) {
				getTexts(Hub, Filename);
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void getTexts(DataExchange Hub, String Filename) {
		testTextsWrapper(Filename, Hub);
	}
	
	public static void upload(URL url, File file, String id) throws IOException, URISyntaxException {
		HttpClient client = new DefaultHttpClient(); // The client object which will do the upload
		HttpPost httpPost = new HttpPost(url.toURI()); // The POST request to send

		FileBody fileB = new FileBody(file);
		System.out.println("Uploading: " + fileB.getFilename());
		MultipartEntityBuilder request = MultipartEntityBuilder.create(); // The HTTP entity which will holds the different body parts,
															// here the file
		request.addPart("file", fileB);
		request.addPart("id", new StringBody(id, ContentType.TEXT_PLAIN));
		httpPost.setEntity(request.build());
		HttpResponse response = client.execute(httpPost); // Once the upload is complete (successful or not), the client
															// will return a response given by the server

		if (response.getStatusLine().getStatusCode() == 200) { // If the code contained in this response equals 200,
																// then the upload is successful (and ready to be
																// processed by the php code)
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(response.getEntity().getContent()));
			String inputLine;
			StringBuffer Response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				Response.append(inputLine);
			}
			in.close();
		}
	}


	private static Boolean testTextsWrapper(String FoundIn, DataExchange Hub) {

		Set<TextElement> Texts = new HashSet<>(getAllTextNodes(new ArrayList<>(), FoundIn, Hub));
		for (TextElement Value : Texts) {
			try {
				Hub.addText(Value);
			} catch (Exception e) {

			}
		}

		return true;

	}

	public static String getDate(long Offset) {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		date.setTime(Offset);
		String CurrentDate = dateFormat.format(date);
		return CurrentDate;
	}

	public static String getDate(long Offset, String Format) {
		DateFormat dateFormat = new SimpleDateFormat(Format);
		Date date = new Date();
		date.setTime(Offset);
		String CurrentDate = dateFormat.format(date);
		return CurrentDate;
	}

	private static List<TextElement> getAllTextNodes(List<TextElement> KnownTextElements, String FoundIn,
			DataExchange Hub) {
		JavascriptExecutor JSE = (JavascriptExecutor) Hub.getDriver();
		String script = "var vals = []; "
					  + "document.querySelectorAll(\"body *:not(i):not(script)\").forEach("
					  +     "x=>{"
					  +         "var xVal = x.getBoundingClientRect().x;"
					  +         "var yVal = x.getBoundingClientRect().y;"
					  +         "if(!x.innerHTML.match(\".*<.*>,*\") && !vals.includes(x.innerHTML) && "
					  +         "xVal != undefined &&yVal!=undefined)"
					  +              "vals.push(x.innerHTML+ \"__SPLITPOINT__x=\" + xVal + \"&y=\"+ yVal)});"
					  + "return vals";
		List<String> TextBlocks = (List<String>) JSE.executeScript(script);
		for (String Node : TextBlocks) {
			try {
				String Text = Node.split("__SPLITPOINT__")[0];
				String loc = Node.split("__SPLITPOINT__")[1];
				int xPos = (int)Float.parseFloat(loc.split("&")[0].replaceAll("x=", ""));
				int yPos = (int)Float.parseFloat(loc.split("&")[1].replaceAll("y=", ""));
				Point elemLoc = new Point(xPos, yPos);
				
				if (Text.matches("(.*)[a-zA-Z0-9]+(.*)")) {
					TextElement t = new TextElement(Text.replaceAll("\"", "\\\""), elemLoc, FoundIn);
					KnownTextElements.add(t);
					System.out.println(t.toString());
				}
			} catch (StaleElementReferenceException Se) {
				break;
			} catch (Exception e) {

			}
		}
		return KnownTextElements;
	}

	public static String getRandomRGBColor() {
		Random R = new Random();
		int Val1 = R.nextInt(255);
		int Val2 = R.nextInt(255);
		int Val3 = R.nextInt(255);
		return "rgb("+Val1+", "+Val2 + ", "+Val3+")";
	}
	
	public static String getRandomRGBAColor() {
		Random R = new Random();
		int Val1 = R.nextInt(255);
		int Val2 = R.nextInt(255);
		int Val3 = R.nextInt(255);
		String Val4 = "0." + R.nextInt(10); 
		return "rgba("+Val1+", "+Val2 + ", "+Val3+", " + Val4 + ")";
	}
	
	
	
	public static int nthIndexOf(String Haystack, String Needle, int Occurences) {
		int Index = 0;
		for(int i = 0; i< Haystack.length(); i++) {
			if((Haystack.charAt(i)+"").equals(Needle)) Index++;
			if(Index == Occurences) return i;
		}
		return -1337;
	}


	
	
	public static String convertHexDezToRGB(String HexDez) {
		int Val1=0, Val2=0, Val3=0;
		
		if(HexDez.matches("#[0-9a-fA-F]{3}")) {
			HexDez = HexDez.replace("#", "");
			Val1 = Integer.valueOf(HexDez.charAt(0)+"", 16);
			Val2 = Integer.valueOf(HexDez.charAt(1)+"", 16);
			Val3 = Integer.valueOf(HexDez.charAt(2)+"", 16);
		}else if(HexDez.matches("#[0-9a-fA-F]{6}")) {
			HexDez = HexDez.replace("#", "");
			Val1 = Integer.valueOf(""+HexDez.charAt(0)+HexDez.charAt(1), 16);
			Val2 = Integer.valueOf(""+HexDez.charAt(2)+HexDez.charAt(3), 16);
			Val3 = Integer.valueOf(""+HexDez.charAt(4)+HexDez.charAt(5), 16);
		}else if(HexDez.matches("#[0-9a-fA-F]{8}")) {
			HexDez = HexDez.replace("#", "");
			Val1 = Integer.valueOf(""+HexDez.charAt(0)+HexDez.charAt(1), 16);
			Val2 = Integer.valueOf(""+HexDez.charAt(2)+HexDez.charAt(3), 16);
			Val3 = Integer.valueOf(""+HexDez.charAt(4)+HexDez.charAt(5), 16);
		}else {
			return HexDez;
		}
		return "rgb("+Val1+", "+Val2 + ", "+Val3+")";
	}
	
	public static String getRandomTime(Boolean TwentyfourHours) {
		if (TwentyfourHours) {
			Random R = new Random();
			String Hours = (R.nextInt(24)) + "";
			String Minute = (R.nextInt(60)) + "";
			if (Hours.length() == 1)
				Hours = "0" + Hours;
			if (Minute.length() == 1)
				Minute = "0" + Minute;
			return Hours + ":" + Minute;
		}
		Random R = new Random();
		String Hours = (R.nextInt(12) + 1) + "";
		String Minute = (R.nextInt(60)) + "";
		if (Hours.length() == 1)
			Hours = "0" + Hours;
		if (Minute.length() == 1)
			Minute = "0" + Minute;
		String AmPm = (R.nextBoolean()) ? "am" : "pm";
		return Hours + ":" + Minute + AmPm;
	}

}
