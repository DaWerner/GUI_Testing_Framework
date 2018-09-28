package Controls;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import Utilities.DataExchange;
import Utilities.MethodTemplates;
import Utilities.UtilityFunctions;

public class GoogleSearchInterface {

	private WebElement inputBox, searchBtn, feelingLuckyBtn, otherServicesBtn, accountBtn, gmailLink, imagesLink,
			notifsbtn, countryInfo, servicesRegion;
	private ServicesField services;
	private DataExchange hub;
	private HashMap<String, Integer> AvailableElements;

	public GoogleSearchInterface(DataExchange hub) {
		this.hub = hub;
		this.AvailableElements = new HashMap<>();
		this.defineElements();
	}

	private void defineElements() {
		MethodTemplates mt = new MethodTemplates(hub);
		AvailableElements.put("inputBox",
				((this.inputBox = mt.defineElement(By.cssSelector("input#lst-ib"))) != null) ? 1 : 0);

		AvailableElements.put("searchBtn",
				((this.searchBtn = mt.defineElement(By.cssSelector("input[type='submit'][name='btnK']"))) != null) ? 1
						: 0);

		AvailableElements.put("feelingLuckyBtn",
				((this.feelingLuckyBtn = mt.defineElement(By.cssSelector("input[type='submit'][name='btnI']"))) != null)
						? 1
						: 0);

		AvailableElements.put("otherServicesBtn",
				((this.otherServicesBtn = mt
						.defineElement(By.xpath("//a[contains(@href, 'options') and @role='button']"))) != null) ? 1
								: 0);

		AvailableElements.put("notifsbtn",
				((this.notifsbtn = mt.defineElement(By.xpath("//a[@class='gb_b' and @role='button']"))) != null) ? 1
						: 0);

		AvailableElements.put("gmailLink",
				((this.gmailLink = mt.defineElement(By.xpath(
						"//div[@id='gbw']//a[contains(@href, 'mail.google') and not(@draggable='false')]"))) != null)
								? 1
								: 0);

		AvailableElements
				.put("imagesLink",
						((this.imagesLink = mt
								.defineElement(By.xpath("//div[@id='gbw']//a[contains(@href, 'imghp')]"))) != null) ? 1
										: 0);
		AvailableElements.put("servicesRegion",
				((this.servicesRegion = mt.defineElement(By.xpath("//div[@id='gbw']//div[@role='region']"))) != null)
						? 1
						: 0);
	}

	public Boolean writeSearchTerm(String toSearch) {
		return new MethodTemplates(hub).writeValue(toSearch, inputBox);
	}

	public Boolean openServices() {
		try {
			this.otherServicesBtn.click();
			this.services = new ServicesField(servicesRegion, hub);
			return this.servicesRegion.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public Boolean search() {
		try {
			String before = hub.getDriver().getCurrentUrl();
			this.searchBtn.click();
			int breakOff = 0;
			while (hub.getDriver().getCurrentUrl().equals(before) && breakOff < 10) {
				Thread.sleep(100);
				++breakOff;
			}
			return !(before.equals(hub.getDriver().getCurrentUrl()));
		} catch (Exception e) {
			return false;
		}
	}

	public Boolean checkAllServiceLinks(String service) {
		if(this.services == null) throw new IllegalStateException("Called resource has not been initialized yet");
		return this.services.serviceLinkWorks(UtilityFunctions.getBaseURL(service));
	}
	
	public Set<String> getServices(){
		if(this.services == null) throw new IllegalStateException("Called resource has not been initialized yet");
		return services.getServices();
	}
	
	public Boolean clickService(String service) {
		if(this.services == null) throw new IllegalStateException("Called resource has not been initialized yet");
		return services.clickService(service);
		
	}
	
	public List<String> getMissingElements() {
		return new MethodTemplates(hub).notAvailable(AvailableElements);
	}
}

class ServicesField {
	private HashMap<String, WebElement> offeredServices;
	private WebElement moreBtn;
	private DataExchange hub;

	public ServicesField(WebElement root, DataExchange hub) {
		offeredServices = new HashMap<>();
		this.hub = hub;
		this.defineServices(root);
	}

	private void defineServices(WebElement root) {
		List<WebElement> services = root.findElements(By.xpath("./ul[not(@aria-hidden='true')]/li/a"));
		for (WebElement service : services) {
			String url = service.getAttribute("href");
			offeredServices.put(url, service);
		}
	}
	
	Boolean clickService(String service) {
		try {
			if(this.offeredServices.get(service) == null)
				throw new IllegalArgumentException("Passed service " + service + " is not available\n"
						+ "use getServices() to access a set of available Services");
			
			this.offeredServices.get(service).click();
			return this.hub.getDriver().getCurrentUrl().contains(service);
		} catch (Exception e) {
			return false;
		}
	}
	
	Boolean serviceLinkWorks(String service) {
		return new MethodTemplates(hub).checkResourceAvailability(service);
	}
	
	Set<String> getServices(){
		return this.offeredServices.keySet();
	}
}
