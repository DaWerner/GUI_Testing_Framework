package Utilities;

import org.apache.commons.text.StringEscapeUtils;
import org.openqa.selenium.Point;

public class TextElement {
	private String Text;
	private Point Location;
	private String Filename;

	public TextElement(String Text, Point Local, String Screenshot) {
		this.Text = StringEscapeUtils.escapeJson(Text);
		this.Filename = Screenshot;
		this.Location = Local;
	}

	public String getText() {
		return this.Text;
	}
	
	@Override
	public String toString() {
		String Formatted = "{\"Text\": \"" + this.Text + "\", \"Screenshot \": \"" + this.Filename
				+ "\", \"Location\": {\"X\": " + this.Location.getX() + ", \"Y\": " + this.Location.getY() + "}}";
		return Formatted;
	}

}
