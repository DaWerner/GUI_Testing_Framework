package Controls;

import java.util.HashMap;
import java.util.List;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Utilities.DataExchange;
import Utilities.MethodTemplates;

public class GoogleSearchInterface {

	private WebElement inputBox, searchBtn, feelingLuckyBtn, otherServicesBtn, accountBtn, gmailLink, imagesLink,
			notifsbtn, countryInfo;
	private ServicesField services;
	private AccountDetails acc;
	private DataExchange hub;
	private HashMap<String, Integer> AvailableElements;

	private void defineElements() {
		MethodTemplates mt = new MethodTemplates(hub);
		AvailableElements.put("inputBox",
				((this.inputBox = mt.defineElement(By.cssSelector("input#lst-ib"))) != null) ? 1 : 0);
		
		AvailableElements.put("searchBtn",
				((this.searchBtn = mt.defineElement(By.cssSelector("input[type='submit'][name='btnK']"))) != null) ? 1: 0);
		
		AvailableElements.put("feelingLuckyBtn",
				((this.feelingLuckyBtn = mt.defineElement(By.cssSelector("input[type='submit'][name='btnI']"))) != null)? 1: 0);
		
		AvailableElements.put("otherServicesBtn",
							 ((this.otherServicesBtn = mt
						.defineElement(By.xpath("//a[contains(@href, 'options') and @role='button']"))) != null) ? 1 : 0);
		
		AvailableElements.put("notifsbtn",
							 ((this.notifsbtn = mt
							 .defineElement(By.xpath("//a[@class='gb_b' and @role='button']"))) != null) ? 1 : 0);
		
		AvailableElements.put("gmailLink",
							 ((this.gmailLink = mt
							 .defineElement(By.xpath("//div[@id='gbw']//a[contains(@href, 'mail.google') and not(@draggable='false')]"))) != null) ? 1 : 0);
		
		AvailableElements.put("imagesLink",
							 ((this.imagesLink = mt
							 .defineElement(By.xpath("//div[@id='gbw']//a[contains(@href, 'imghp')]"))) != null) ? 1 : 0);

	}
	
	public Boolean writeSearchTerm(String toSearch) {
		return new MethodTemplates(hub).writeValue(toSearch, inputBox);
	}

	public Boolean search() {
		try {
			String before = hub.getDriver().getCurrentUrl();
			this.searchBtn.click();
			int breakOff = 0;
			while(hub.getDriver().getCurrentUrl().equals(before) && breakOff < 10) {
				Thread.sleep(100);
				++breakOff;
			}
			return !(before.equals(hub.getDriver().getCurrentUrl()));
		} catch (Exception e) {
			return false;
		}
	}
	
	public List<String> getMissingElements(){
		return new MethodTemplates(hub).notAvailable(AvailableElements);
	}
}


class ServicesField {
	private HashMap<String, WebElement> offeredServices;
	private WebElement moreBtn;

	public ServicesField(WebElement root) {
		// TODO Auto-generated constructor stub
	}
}

class AccountDetails {
	private WebElement accountPP, emailSpan, accountName, google_plusLink, addAccBtn;

	public AccountDetails() {
		// TODO Auto-generated constructor stub
	}
}