import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Calendar;

class CnCServer implements Runnable {
	// TODO connection to CnC should be IP address whitelisted;
	// set address when starting this client from command line
	public CnCServer(String argv[]) {
	}

	public void run() {
		try {
			ServerSocket socket = new ServerSocket(6789);
		while (true) {
			String line;
			try {
				Socket connectionSocket = socket.accept();
				BufferedReader inFromClient = new BufferedReader(
						new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(
						connectionSocket.getOutputStream());
				line = inFromClient.readLine();
				String response = "";
				try {
					if (line.substring(0, 3).contentEquals("GET")) {
						System.out.println("CnCServer(): received GET request");
						int cmdStart = line.indexOf("f=") + 2;
						int cmdEnd = line.indexOf(" ", cmdStart);
						String cmd = line.substring(cmdStart, cmdEnd);
						System.out.println("CnCServer(): received command: " + cmd);
						String content = cmdExec(cmd);
						response = "HTTP/1.1 200 OK\r\n"
								+ "Date: xxxxxxxxxxx\r\n"
								+ "Server: xxxxxxxxxx\r\n"
								+ "Content-Length: " + content.length() + "\r\n"
								+ "Connection: close\r\n"
								+ "Content-Type: text/html\r\n\r\n" + content;
					}
				} catch (IndexOutOfBoundsException e) {
					// malformed request - ignore
				}
				outToClient.writeBytes(response);
				//connectionSocket.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		} catch(IOException e) {
			// socket setup error
			e.printStackTrace();
		}
	}

	String cmdExec(String cmd) {
		String res = "";
		if (cmd.contentEquals("listclients")) {
			System.out.println("cmdExec(): executing " + cmd);
			ArrayList<PlayerEntity> players = World.getPlayerEntities();
			res = "clientnum,index,team,squad,nickname<br />";
			PlayerEntity e;
			for (int i = 0; i < players.size(); ++i) {
				e = players.get(i);
				res += String.format("%9d,", e.clientnum) + " "  
						+ String.format("%5d,", e.entityIndex) + " " 
				+ String.format("%4d,", e.team) + " " 
				+ String.format("%4d,", e.squad) +  " " 
				+ String.format("%s", e.nickname) + "<br />";
			}
		}
		return res;
	}
}