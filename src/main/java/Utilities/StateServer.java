package Utilities;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class StateServer{
	private DataExchange Hub;
	public StateServer(DataExchange Hub) {
		this.Hub = Hub;
	}

    static class MyHandler implements HttpHandler {
    	private String command;
    	private DataExchange Hub;
    	
    	public MyHandler(String command, DataExchange Hub) {
			this.Hub = Hub;
			this.command = command;
		}
    	
    	private String getTodos() {
    		String toRet = "";
    		int count = 0;
    		for(String arg : Hub.getArguments()) {
    			if(Hub.getFinishedTestcases().contains(arg)) continue;
    			toRet += "\""+arg +"\",";
    			count++;
    		}
    		String response = "{\"todo\":"+ count+ ", \"list\":["+ toRet.substring(0, toRet.length()-1) + "]}";
    		return response;
    	}
    	
    	
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "";
            if(this.command.equals("todo")) response = getTodos();
            else if(this.command.equals("result")) response = Hub.finalizeReport();
            t.sendResponseHeaders(200, response.getBytes("utf-8").length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

	public void start(){
        HttpServer server;
		try {
			server = HttpServer.create(new InetSocketAddress(1337), 0);
	        server.createContext("/todo", new MyHandler("todo", Hub));
	        server.createContext("/result", new MyHandler("result", Hub));
	        server.setExecutor(null); // creates a default executor
	        server.start();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
}
