package beta.server;

import java.io.DataOutputStream;
import java.net.Socket;


public class SendFile {

	
	private Socket socket;
	private String file;
	private String filename;

	public SendFile(Socket socket, String filename, String file) {
		this.file = file;
		this.socket = socket;
		this.filename = filename;
	}
	
	
	public boolean sendFile(String path) {
		
		try {
			
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			output.writeUTF("#newfile " + filename);
			output.writeUTF(file);
			
			return true;
			
			
		} catch (Exception e) {
			return false;
		}
		
		
	}
	
}
