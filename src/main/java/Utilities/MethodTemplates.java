package Utilities;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;

public class MethodTemplates {

	private WebDriver driver;
	private DataExchange Hub;
	
	public MethodTemplates(DataExchange Hub) {
		this.Hub = Hub;
		this.driver = Hub.getDriver();
	}
	
	
	
	public Boolean selectText(String Selector) {
		String Script = "	function fnSelect() {\n" + 
						"		fnDeSelect();\n" + 
						"		if (document.selection) {\n" + 
						"		var range = document.body.createTextRange();\n" + 
						" 	        range.moveToElementText(document.querySelector('"+Selector+"'));\n" + 
						"		range.select();\n" + 
						"		}\n" + 
						"		else if (window.getSelection) {\n" + 
						"		var range = document.createRange();\n" + 
						"		range.selectNode(document.querySelector('"+Selector+"'));\n" + 
						"		window.getSelection().addRange(range);\n" + 
						"		}\n" + 
						"	}\n" + 
						" \n" + 
						"	function fnDeSelect() {\n" + 
						"		if (document.selection) document.selection.empty(); \n" + 
						"		else if (window.getSelection)\n" + 
						"                window.getSelection().removeAllRanges();\n" + 
						"	}"
						+ ""
						+ "fnSelect();";
		try {
			((JavascriptExecutor) driver).executeScript(Script);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	/**
	 * Method to determine if an error is displayed
	 * @param El Element that should contain the ui-state-error class
	 * @return true if error class is present and it's values are not overwritten by another css style
	 */
	public Boolean checkErrorState(WebElement El) {
		try {
			Boolean hasClass =  El.getAttribute("class").contains("ui-state-error");
			String ColorVal =(String) ((JavascriptExecutor) driver).executeScript("return window.getComputedStyle(arguments[0], null).color", El);
			Boolean isRed = ColorVal.contains("rgb(244, 67, 54)")  ;
			return hasClass && isRed;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Method to select a specific option
	 * @param ToChoose title of desired dropdown element (equals text in english)
	 * @return true if desired Element is active afterwards
	 */
	public Boolean selectFromHashmap(String ToChoose, HashMap<String, WebElement> Map, WebElement Parent) {
		try {
			Parent.click();
			Map.get(ToChoose).click();
			return Map.get(ToChoose).getAttribute("class").contains("ui-state-active");
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Method to click a checkbox which switches from the "ui-icon-on" to the ui-icon-off" css class
	 * @param El Webelement which contains the state information
	 * @return true if Checkbox is active afterwards, otherwise false
	 */
	public Boolean clickCheckboxUiIconOn(WebElement El) {
		try {
			El.click();
			return El.getAttribute("class").contains("cm_ui-icon-checkbox-on");
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Method to click a checkbox which switches from the "ui-icon-on" to the ui-icon-off" css class
	 * @param El Webelement which contains the state information
	 * @return true if Checkbox is active afterwards, otherwise false
	 */
	public Boolean clickRadioUiIconOn(WebElement El) {
		try {
			El.click();
			return El.getAttribute("class").contains("ui-icon-radio-on");
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Method to click a checkbox which doesn't show a change in css classes
	 * @param Actual input element of the checkbox
	 * @return true if Checkbox is active afterwards, otherwise false
	 */
	public Boolean clickCheckboxCheckedAttribute(WebElement El) {
		try {
			El.findElement(By.xpath("..")).click();
			return ((Boolean) ((JavascriptExecutor) driver).executeScript("return arguments[0].checked;", El));
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Method to write a value into an textfield
	 * @param Val Value to write
	 * @param El WebElement to write the passed vlaue into
	 * @return true if value of passed element contains passed Val
	 */
	public Boolean writeValue(String Val, WebElement El) {
		try {
			El.clear();
			El.sendKeys(Val);
			Boolean Written = (El.getAttribute("value")==null)?El.getAttribute("innerHTML").contains(Val):El.getAttribute("value").contains(Val);
			return Written;
		} catch (Exception e) {
			return false;
		}
	}
	
	public String selectImageFromCarrousel(WebElement Img, WebElement ScrollerLeft, WebElement ScrollerRight) {
		try {
			Boolean Success = this.scrollImageIntoView(Img, ScrollerLeft, ScrollerRight);
			Thread.sleep(3000);
			Img.click();
			if(Success && Img.findElement(By.xpath("./../..")).getAttribute("class").contains("ui-state-active"))
				return Img.getAttribute("src");
		} catch (Exception e) {
			return null;
		}
		return null;
	}
	
	private Boolean scrollImageIntoView(WebElement Img, WebElement ScrollerLeft, WebElement ScrollerRight) {
		try {
			int BorderLeft = ScrollerLeft.getLocation().getX();
			int BorderRight = ScrollerRight.getLocation().getX();
			int ImgLoc = Img.getLocation().getX();
			if(ImgLoc > BorderLeft && ImgLoc < BorderRight) {
				return true;
			}
			while(!(ImgLoc > BorderLeft && ImgLoc < BorderRight)) {
				if(ImgLoc > BorderRight)
					ScrollerRight.click();
				if(ImgLoc < BorderLeft)
					ScrollerLeft.click();
				ImgLoc = Img.getLocation().getX();
			}
			return (ImgLoc > BorderLeft && ImgLoc < BorderRight);
		} catch (Exception e) {
			UtilityFunctions.takeScreenshot(Hub, "error" + Hub.getErrorCount());
			return false;
		}
	}

	/**
	 * Checks if passed URL responds outside of error range. 
	 * @param Src String representing a URL to check
	 * @return true if Reponsecode < 400, otherwise false
	 */
	public Boolean checkResourceAvailability(String Src) {
		try {
			HttpURLConnection Conn = (HttpURLConnection) new URL(Src).openConnection();
			return Conn.getResponseCode() < 400;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Method to close the Widget config via passed button
	 * @return true if the button could be clicked and it's non existence could be asserted
	 */
	public Boolean closeWidget(WebElement Btn) {
		try {
			Btn.click();
			Thread.sleep(1000);
			return !this.Hub.getContext().switchToWidgetFrame();
		} catch (Exception e) {
			return false;
		}
	}
	
	public WebElement defineElement(By sel) {
		try {
			return Hub.getDriver().findElement(sel);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	public List<String> notAvailable(HashMap<String, Integer> AvailableElements) {
		ArrayList<String> toRet = new ArrayList<>();
		for(String val : AvailableElements.keySet()) {
			if(AvailableElements.get(val) == 0) toRet.add(val);
		}
		return toRet;
	}
	
	/**
	 * Method to define all options visible after the passed webelement has been clicked
	 * @param DD Dropdown to click
	 * @return HashMap mapping an option to it's title
	 */
	public HashMap<String, WebElement> defineOptions(WebElement DD){
		HashMap<String, WebElement> ToRet = new HashMap<>();
		try {
		DD.click();
		for(WebElement Option: driver.findElements(By.className("cm_ui-list-item"))) {
			try {
				if(Option.isDisplayed()) {
					ToRet.put(Option.getAttribute("title"), Option);
				}
			} catch (Exception e) {}
		}
		driver.findElement(By.className("cm_ui-dialog-header_box")).click();
		UtilityFunctions.takeScreenshot(Hub, "Options");
		}catch (Exception e) {}
		return ToRet;
	}
	
	public Boolean checkAvailabilityFromInsideAUT(String link) {
		String Script = "var oReq = new XMLHttpRequest();oReq.open('GET', 'arguments[0]');try{oReq.send(); return true}catch{return false}";
		return (Boolean) ((JavascriptExecutor) driver).executeScript(Script, link);
	}
	
}

