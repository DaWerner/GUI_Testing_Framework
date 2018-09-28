package Utilities;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CLIParser {

	protected String Browser, Server, MailAddress, HubURL, Account, Language, Team, TestType, BackendURL;
	protected Boolean Screenshots, LangCheck;
	protected ArrayList<String> Arguments;

	public CLIParser(String[] Args) {
		this.Browser = "Chrome";
		this.MailAddress = "";
		this.Team = "QA";
		this.Server = "";
		this.Arguments = new ArrayList<>();
		this.Account = "";
		this.Screenshots = true;
		this.LangCheck = false;
		this.BackendURL = "none";
		this.HubURL = "http://172.30.4.23:4444/wd/hub";
		this.Language = "en";
		this.TestType = "standard";
		this.parseCLI(Args);
	}

	/**
	 * Reads arguments and parses them to variables
	 * @return ArrayList containing TestCases
	 */
	private void parseCLI(String[] args) {
		List<String> Keywords = new ArrayList<>();
		for (int Iter = 0; Iter < args.length; Iter++) {
			if (args[Iter].startsWith("--")) {
				String Order = args[Iter];
				int Add = 1;
				while ((Iter + Add) < args.length && !args[Iter + Add].startsWith("--")) {
					Order += " " + args[Iter + Add];
					Add++;
				}

				Keywords.add(Order);

			}
		}

		for (String Param : Keywords) {
			String Keyword = (Param.startsWith("--") ? Param.split(" ")[0] : "");

			try {

				if (Keyword.equalsIgnoreCase("--Browser"))
					Browser = Param.split(" ")[1];

				if (Keyword.equalsIgnoreCase("--Server"))
					Server = Param.split(" ")[1];
				if (Keyword.equalsIgnoreCase("--help")) {
					System.exit(0);
				}
				if (Keyword.equalsIgnoreCase("--Team"))
					Team = Param.split(" ")[1];

				if (Keyword.equalsIgnoreCase("--Email"))
					MailAddress = Param.split(" ")[1];

				if (Keyword.equalsIgnoreCase("--HubUrl"))
					HubURL = Param.split(" ")[1];

				if (Keyword.equalsIgnoreCase("--AccountName"))
					Account = Param.split(" ")[1];
				
				if (Keyword.equalsIgnoreCase("--Language"))
					Language = Param.split(" ")[1];

				if (Keyword.equalsIgnoreCase("--Screenshots"))
					Screenshots = Param.split(" ")[1].equals("true");
				
				if(Keyword.equalsIgnoreCase("--Backend"))
					BackendURL = Param.split(" ")[1];
				
				if (Keyword.equalsIgnoreCase("--checkLang"))
					LangCheck = Param.split(" ")[1].equals("true");

				if (Keyword.equals(""))
					System.out.println("<Info> Passed Parameter " + Param + "is not well formed </Info>");

				if (Keyword.equalsIgnoreCase("--Testcases")) {
					for (String Value : Param.split(" ")) {
						if (!Value.startsWith("--")) {
							Arguments.add(Value);
						}
					}
				}


			} catch (Exception e) {
				System.out.println("<Info> Parameter" + Keyword + " expects Argument(s) </Info>");
			}

		}
	}
	
}
