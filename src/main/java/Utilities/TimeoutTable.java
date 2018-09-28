
package Utilities;

import java.util.HashMap;

public class TimeoutTable {
	private HashMap<String, Integer> Timeouts;
	
	public TimeoutTable(Boolean Editor) {
		this.Timeouts =(Editor)? fillTimeoutsEditor() : fillTimeoutsPublished();
	}
	
	public int getTimeout(String Test) {
		if(Timeouts.get(Test) == null) return 90;
		else return Timeouts.get(Test);
	}
	
	private HashMap<String, Integer> fillTimeoutsEditor(){
		HashMap<String, Integer> Tests = new HashMap<>();
		Tests.put("SupportAir", 90);
		Tests.put("Preview", 90);
		Tests.put("Title", 90);
		Tests.put("Toolbar", 90);
		Tests.put("Contact_Form", 180);
		Tests.put("Reservation", 180);
		Tests.put("Proposal_Request", 180);
		Tests.put("Meeting_Request", 180);
		Tests.put("Call-back_Form", 180);
		Tests.put("Room_Reservation", 180);
		Tests.put("Our_Team", 240);
		Tests.put("Portfolio", 240);
		Tests.put("WidgetsTab", 180);
		Tests.put("Colors", 300);
		Tests.put("Fonts", 300);
		Tests.put("Sitemap", 90);
		Tests.put("Ticker", 90);
		Tests.put("Podcast", 90);
		Tests.put("RSS_Feed", 90);
		Tests.put("Amazon", 360);
		Tests.put("Ecwid_Store", 90);
		Tests.put("Photo_Gallery", 120);
		Tests.put("Photo_Grid", 120);
		Tests.put("Photo_Slideshow", 120);
		Tests.put("Search", 90);
		Tests.put("Shop", 180);
		Tests.put("Twitter", 90);
		Tests.put("Download", 90);
		Tests.put("Facebook_Page", 90);
		Tests.put("Blog", 300);
		Tests.put("Guestbook", 90);
		Tests.put("Map/Directions", 90);
		Tests.put("Map", 90);
		Tests.put("OpenTable", 90);
		Tests.put("Languages", 240);
		Tests.put("Ebay", 90);
		Tests.put("Ebay_Product", 90);
		Tests.put("Embed", 90);
		Tests.put("News",90);
		Tests.put("Event_Calendar",90);
		Tests.put("Voting",90);
		Tests.put("PhpBB3",90);
		Tests.put("Brillux_Farbdesigner", 90);
		Tests.put("Albis", 90);
		Tests.put("CompuMed", 90);
		Tests.put("TurboMed", 90);
		Tests.put("LifeEServices", 90);
		Tests.put("DataVital", 90);
		Tests.put("MediStar", 90);
		Tests.put("PraxisPlus", 90);
		Tests.put("Chremasoft", 90);
		Tests.put("JabInteria", 90);
		Tests.put("Separating_Line", 90);
		Tests.put("Google_Calendar", 90);
		Tests.put("Opening_Hours", 120);
		Tests.put("Counter", 90);
		Tests.put("CheckIn_Hours", 120);
		Tests.put("Office_Hours", 180);
		Tests.put("Consultation_Hours", 180);
		Tests.put("Opening_Hours", 180);
		Tests.put("Share", 90);
		Tests.put("Facebook_Like", 90);
		Tests.put("Xing_Share", 90);
		Tests.put("Google_Plus", 90);
		Tests.put("LinkedIn_Share", 90);
		Tests.put("Twitter_Share", 90);
		Tests.put("Media_Feed", 90);
		Tests.put("Button", 90);
		Tests.put("Ambiente_Online", 90);
		Tests.put("Hotel_Reservation", 90);
		Tests.put("RealEstate_Form", 120);
		Tests.put("Insurance_Form", 120);
		Tests.put("Automobile_Catalog", 90);
		Tests.put("Belbo", 90);
		Tests.put("Hotelde_Siegel", 90);
		Tests.put("Sihot", 90);
		Tests.put("Weinor_Produkt", 90);
		Tests.put("HomeTrendBerater_Planer", 90);
		Tests.put("HomeTrendBerater_Magazin", 90);
		Tests.put("ZDH_Campaign", 90);
		Tests.put("Weinor_Configurator", 90);
		Tests.put("WuKVersicherungslexikon", 90);
		Tests.put("WuKFinanzlexikon", 90);
		Tests.put("Anwalt24", 90);
		Tests.put("HotelDeReviews", 90);
		Tests.put("Datev_Banner", 90);
		Tests.put("Datev_Button", 90);
		Tests.put("Datev_Teaser", 90);
		Tests.put("Datev_Element", 90);
		Tests.put("DEHOGA", 90);
		Tests.put("Datev_Rss", 90);
		Tests.put("Tax_Calendar", 90);
		Tests.put("CaesarData", 90);
		Tests.put("Cultuzz", 90);
		Tests.put("Teckentrup", 90);
		Tests.put("Dr_Grandel", 90);
		Tests.put("Ibelsa", 90);
		Tests.put("Terminland", 90);
		Tests.put("OnlineRes", 90);
		Tests.put("Dirs21", 90);		
		Tests.put("Toolbar", 900);
		Tests.put("Rollladenberater", 90);
		Tests.put("Samedi", 90);
		Tests.put("Resmio", 90);
		Tests.put("Yovite", 90);
		Tests.put("AmbienteShop", 90);
		Tests.put("RetailServices", 90);
		Tests.put("HotelServices", 90);
		Tests.put("Soundcloud", 90);
		Tests.put("Mixcloud", 90);
		Tests.put("Youtube_Video", 90);
		Tests.put("Vimeo_Video", 90);
		Tests.put("UROPhotoExternalLinkPopupSmall", 90);
		Tests.put("UROPhotoExternalLinkPopupMedium", 90);
		Tests.put("UROPhotoExternalLinkPopupLarge", 90);
		Tests.put("UROPhotoExternalLinkPopupCustom", 90);
		Tests.put("UROPhotoEmailLink", 90);
		Tests.put("UROPhotoFullScreen", 90);
		Tests.put("UROPhotoInternalLink", 90);
		Tests.put("HelpCenter", 180);
		Tests.put("CmEmpty", 90);
		Tests.put("Logo", 90);
		Tests.put("FileManager", 90);
		Tests.put("ColumnEditor", 90);
		Tests.put("Colors", 300);
		Tests.put("Fonts", 180);
		Tests.put("Job_Offers_Building", 300);
		Tests.put("Epages", 600);
		Tests.put("BackgroundGif", 600);
		Tests.put("Ticker", 600);
		return Tests;
	}
	
	
	private HashMap<String, Integer> fillTimeoutsPublished(){
		HashMap<String, Integer> Tests = new HashMap<>();
		Tests.put("SupportAir", 90);
		Tests.put("Preview", 90);
		Tests.put("Title", 90);
		Tests.put("Toolbar", 90);
		//Forms
		//Tests.put("Contact_Form", 90);
		Tests.put("Reservation", 90);
		Tests.put("Proposal_Request", 90);
		Tests.put("Meeting_Request", 90);
		Tests.put("Call-back_Form", 90);
		Tests.put("Room_Reservation", 90);

		//Catalogs
		Tests.put("Our_Team", 120);
		Tests.put("Portfolio", 120);
		
		//Others
		Tests.put("Sitemap", 90);
		Tests.put("Ticker", 90);
		Tests.put("Podcast", 90);
		Tests.put("RSS_Feed", 90);
		Tests.put("Amazon", 90);
		Tests.put("Ecwid_Store", 90);
		Tests.put("WebsiteStructure", 900);
		Tests.put("Photo_Gallery", 90);
		Tests.put("Photo_Grid", 90);
		Tests.put("Photo_Slideshow", 90);

		Tests.put("Search", 90);
		
		Tests.put("Shop", 90);
		Tests.put("Twitter", 90);
		Tests.put("Download", 90);
		Tests.put("Facebook_Page", 90);
		Tests.put("Blog", 90);
		Tests.put("Guestbook", 90);
		Tests.put("Map/Directions", 90);
		Tests.put("Map", 90);
		Tests.put("OpenTable", 90);
		Tests.put("Languages", 90);
		Tests.put("Ebay", 90);
		Tests.put("Ebay_Product", 90);
		Tests.put("Embed", 90);
		
		Tests.put("News",90);
		Tests.put("Event_Calendar",90);
		Tests.put("Voting",90);
		Tests.put("PhpBB3",90);
		Tests.put("Brillux_Farbdesigner", 90);
		Tests.put("Albis", 90);
		Tests.put("CompuMed", 90);
		Tests.put("TurboMed", 90);
		Tests.put("LifeEServices", 90);
		Tests.put("DataVital", 90);
		Tests.put("MediStar", 90);
		Tests.put("PraxisPlus", 90);
		Tests.put("Chremasoft", 90);
		
		Tests.put("JabInteria", 90);
		
		
		Tests.put("Separating_Line", 90);
		Tests.put("Google_Calendar", 90);
		Tests.put("Opening_Hours", 90);
		Tests.put("Counter", 90);

		Tests.put("CheckIn_Hours", 90);
		Tests.put("Office_Hours", 90);
		Tests.put("Consultation_Hours", 90);

		Tests.put("Share", 90);
		Tests.put("Facebook_Like", 90);
		Tests.put("Xing_Share", 90);
		Tests.put("Google_Plus", 90);
		Tests.put("LinkedIn_Share", 90);
		Tests.put("Twitter_Share", 90);
		Tests.put("Media_Feed", 90);
		Tests.put("Button", 90);
		Tests.put("Ambiente_Online", 90);
		Tests.put("Hotel_Reservation", 90);
		Tests.put("RealEstate_Form", 90);
		Tests.put("Insurance_Form", 90);
		Tests.put("Automobile_Catalog", 90);
		Tests.put("Belbo", 90);
		Tests.put("Hotelde_Siegel", 90);
		Tests.put("Sihot", 90);
		Tests.put("Weinor_Produkt", 90);
		Tests.put("HomeTrendBerater_Planer", 90);
		Tests.put("HomeTrendBerater_Magazin", 90);
		Tests.put("ZDH_Campaign", 90);
		Tests.put("Weinor_Configurator", 90);
		Tests.put("WuKVersicherungslexikon", 600);
		Tests.put("WuKFinanzlexikon", 600);
		Tests.put("Anwalt24", 90);
		Tests.put("HotelDeReviews", 90);
		Tests.put("Datev_Banner", 90);
		Tests.put("Datev_Button", 90);
		Tests.put("Datev_Teaser", 90);
		Tests.put("Datev_Element", 90);
		Tests.put("DEHOGA", 90);
		Tests.put("Datev_Rss", 90);
		Tests.put("Tax_Calendar", 90);
		Tests.put("CaesarData", 90);
		Tests.put("Cultuzz", 90);
		Tests.put("Teckentrup", 90);
		Tests.put("Dr_Grandel", 90);
		Tests.put("Ibelsa", 90);
		Tests.put("Terminland", 90);
		Tests.put("OnlineRes", 90);
		Tests.put("Dirs21", 90);
		Tests.put("Rollladenberater", 90);
		Tests.put("Samedi", 90);
		Tests.put("Resmio", 90);
		Tests.put("Yovite", 90);
		Tests.put("AmbienteShop", 90);

		//Leistungen
		Tests.put("RetailServices", 90);
		Tests.put("HotelServices", 90);
		
		//Musik
		Tests.put("Soundcloud", 90);
		Tests.put("Mixcloud", 90);
		
		//Videos
		Tests.put("Youtube_Video", 90);
		Tests.put("Vimeo_Video", 90);

		//Editor
		Tests.put("UROPhotoExternalLinkPopupSmall", 90);
		Tests.put("UROPhotoExternalLinkPopupMedium", 90);
		Tests.put("UROPhotoExternalLinkPopupLarge", 90);
		Tests.put("UROPhotoExternalLinkPopupCustom", 90);
		Tests.put("UROPhotoEmailLink", 90);
		Tests.put("UROPhotoFullScreen", 90);
		Tests.put("UROPhotoInternalLink", 90);

		Tests.put("CmEmpty", 90);
		Tests.put("Logo", 90);
		Tests.put("FileManager", 90);
		Tests.put("ColumnEditor", 90);
		
		Tests.put("Fonts", 90);
		
		return Tests;
	}

}

