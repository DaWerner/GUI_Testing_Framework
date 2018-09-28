package Utilities;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import Utilities.DataExchange;
import Utilities.UtilityFunctions;

public class CurrentContext {
	private WebDriver driver;
	private List<String> FrameBC;
	private List<String> ContextLevels;
	private DataExchange Hub;
	Boolean InEditor;
	
	public CurrentContext(DataExchange Hub) {
		this.FrameBC = new ArrayList<>();
		this.ContextLevels = new ArrayList<>();
		this.Hub = Hub;
	}
	
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
	
	
	/**
	 * Adds frame to List FrameBC
	 * @param newFrame - Frame to add
	 */
	void AddFrameLevel(String newFrame) {
		FrameBC.add(newFrame);
	}

	
	/**
	 * changes frame to prev frameLevel and removes current frame from FrameBC 
	 * @return Boolean true on success 
	 */
	Boolean goToPrevFrame() {
		try {
			for (int i = 0; i < this.FrameBC.size()-2; i++) {
				String Level = this.FrameBC.get(i);
				if (Level.equals("Mainframe")) {
					this.switchToMainFrame();
				} else if (Level.equals("WidgetFrame")) {
					this.switchToWidgetFrame();
				} else if (Level.equals("Sitetree")) {
					this.switchToSiteTreeFrame();
				} else if (Level.equals("Filemanager")) {
					this.switchToFilemanger();
				}else if (Level.equals("Colors")) {
					this.switchToDesignColorsFrame();
				}else if(Level.equals("Fonts")) {
					this.switchToDesignFontsFrame();
				}else if(Level.equals("KVConfigFrame")){
					this.switchToKVConfigFrame();
				}
			}
			this.FrameBC.remove(this.FrameBC.size()-1);
			return true;
		} catch (Exception e) {
			UtilityFunctions.takeScreenshot(Hub, "error" + Hub.getErrorCount());
			return false;
		}
	}

	
	
	/**
	 * Switches to DesignColorsFrame and adds Colors FrameLevel to List FrameBC
	 * @return Boolean true on success
	 */
	Boolean switchToDesignColorsFrame() {
		try {
			driver.switchTo().defaultContent();
			Thread.sleep(500);
			List<WebElement> Frames = driver.findElements(By.tagName("iframe"));
			for (WebElement Frame : Frames) {
				if (Frame.getAttribute("src").contains("design-colors-popup.htmlc")) {
					driver.switchTo().frame(Frame);
					this.AddFrameLevel("Colors");
					return true;
				}
			}
		} catch (Exception e) {
			UtilityFunctions.takeScreenshot(Hub, "error" + Hub.getErrorCount());
			return false;
		}
		return false;
	}
	
	/**
	 * Switches to DesignFontsFrame and adds Fonts FrameLevel to List FrameBC
	 * @return Boolean true on success
	 */
	
	Boolean switchToDesignFontsFrame() {
		try {
			driver.switchTo().defaultContent();
			Thread.sleep(500);
			List<WebElement> Frames = driver.findElements(By.tagName("iframe"));
			for (WebElement Frame : Frames) {
				if (Frame.getAttribute("src").contains("design-settings-popup.htmlc")) {
					driver.switchTo().frame(Frame);
					this.AddFrameLevel("Fonts");
					return true;
				}
			}
		} catch (Exception e) {
			UtilityFunctions.takeScreenshot(Hub, "error" + Hub.getErrorCount());
			return false;
		}
		return false;
	}
	
	
	/**
	 * Method to switch to the config frame for Background Content
	 * @return true if desired frame was reached, otherwise false
	 */
	Boolean switchToBackgroundIMageConfig() {
		try {
			driver.switchTo().defaultContent();
			Thread.sleep(500);
			List<WebElement> Frames = driver.findElements(By.tagName("iframe"));
			for (WebElement Frame : Frames) {
				if (Frame.getAttribute("src").contains("background.htmlc")) {
					driver.switchTo().frame(Frame);
					this.AddFrameLevel("Sitetree");
					return true;
				}
			}
		} catch (Exception e) {
			UtilityFunctions.takeScreenshot(Hub, "error" + Hub.getErrorCount());
			return false;
		}
		return false;
	}
	
	/**
	 * Switches to SideTreeFrame and adds Sitetree FrameLevel to List FrameBC
	 * @return Boolean true on success
	 */
	
	Boolean switchToSiteTreeFrame() {
		try {
			driver.switchTo().defaultContent();
			Thread.sleep(500);
			List<WebElement> Frames = driver.findElements(By.tagName("iframe"));
			for (WebElement Frame : Frames) {
				if (Frame.getAttribute("src").contains("sitetree-editor.htmlc")) {
					driver.switchTo().frame(Frame);
					this.AddFrameLevel("Sitetree");
					return true;
				}
			}
		} catch (Exception e) {
			UtilityFunctions.takeScreenshot(Hub, "error" + Hub.getErrorCount());
			return false;
		}
		return false;
	}
	
	/**
	 * Switches to Mainframe and adds Mainframe FrameLevel to List FrameBC
	 * @return Boolean true on success
	 */

	public Boolean switchToMainFrame() {
		try {
			driver.switchTo().defaultContent();
			Thread.sleep(500);
			driver.switchTo().frame(driver.findElement(By.id("main-frame")));
			AddFrameLevel("Mainframe");
			return true;
		} catch (Exception e) {
			UtilityFunctions.takeScreenshot(Hub, "error" + Hub.getErrorCount());
			return false;
		}
	}
	
	/**
	 * Switches to Filemanager and adds Filemanager FrameLevel to List FrameBC
	 * @return Boolean true on success
	 */
	
	Boolean switchToFilemanger() {
		try {
			driver.switchTo().defaultContent();
			Thread.sleep(500);
			List<WebElement> Frames = driver.findElements(By.tagName("iframe"));
			for (WebElement Frame : Frames) {
				if (Frame.getAttribute("src").contains("uro-manage.htmlc")) {
					driver.switchTo().frame(Frame);
					this.AddFrameLevel("Filemanager");
					return true;
				}
			}
		} catch (Exception e) {
			UtilityFunctions.takeScreenshot(Hub, "error" + Hub.getErrorCount());
			return false;
		}
		return false;
	}
	
	Boolean switchToWebsiteStructure() {
		try {
			driver.switchTo().defaultContent();
			Thread.sleep(500);

			List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
			for (WebElement iframe : iframes) {
				if (iframe.getAttribute("src").contains("/.cm4all/s/assets/html/sitetree-editor.htmlc")) {
					driver.switchTo().frame(iframe);
					return true;
				}
			}
		}catch (Exception e) {
			UtilityFunctions.takeScreenshot(Hub, "error" + Hub.getErrorCount());
			return false;
		}
		return false;
	}
	
	public Boolean switchToLogoDialog() {
		try {
			driver.switchTo().defaultContent();
			Thread.sleep(500);

			List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
			for (WebElement iframe : iframes) {
				if (iframe.getAttribute("src").contains("/.cm4all/s/assets/html/editor/logo.htmlc")) {
					driver.switchTo().frame(iframe);
					return true;
				}
			}
		}catch (Exception e) {
			UtilityFunctions.takeScreenshot(Hub, "error" + Hub.getErrorCount());
			return false;
		}
		return false;

	}
	
	public Boolean switchToLogoSettings() {
		try {
			driver.switchTo().defaultContent();
			Thread.sleep(500);

			List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
			for (WebElement iframe : iframes) {
				if (iframe.getAttribute("src").contains("/.cm4all/s/assets/html/editor/logo-settings.htmlc")) {
					driver.switchTo().frame(iframe);
					return true;
				}
			}
		}catch (Exception e) {
			UtilityFunctions.takeScreenshot(Hub, "error" + Hub.getErrorCount());
			return false;
		}
		return false;
	}
	
	
	/**
	 * Switches to WidgetFrame and adds WidgetFrame FrameLevel to List FrameBC
	 * @return Boolean true on success
	 */
	
	
	public Boolean switchToWidgetFrame() {
		Boolean switched = false;
		try {
			driver.switchTo().defaultContent();
			Thread.sleep(500);
			List<WebElement> Frames = driver.findElements(By.tagName("iframe"));
			for (WebElement Frame : Frames) {
				if (Frame.getAttribute("src").contains("WidgetMgr")) {
					driver.switchTo().frame(Frame);
					break;
				}
			}
			//Repeat a second time since the actual iframe containing the config is within an outer iframe
			Frames = driver.findElements(By.tagName("iframe"));
			for (WebElement Frame : Frames) {
				if (Frame.getAttribute("src").contains("WidgetMgr")) {
					driver.switchTo().frame(Frame);
					switched=true;
					break;
				}
			}
			AddFrameLevel("WidgetFrame");
			return switched;
		} catch (Exception e) {
			UtilityFunctions.takeScreenshot(Hub, "error" + Hub.getErrorCount());
			return false;
		}
	}
	
	
	/**
	 * Switches to KVConfigFrame and adds KVConfigFrame FrameLevel to List FrameBC
	 * @return Boolean true on success
	 */
	Boolean switchToKVConfigFrame() {
		try{
			driver.switchTo().defaultContent();
			Thread.sleep(500);
			WebElement configframe=driver.findElement(By.xpath("//iframe[contains(@src,'keyvisual')]"));
			driver.switchTo().frame(configframe);			
			AddFrameLevel("KVConfigFrame");
			return true;
		} catch(Exception e) {
			return false;
		}
		
		
	}
	

}

