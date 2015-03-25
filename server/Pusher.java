package beta.server;

import java.net.Socket;
import java.util.ArrayList;

public class Pusher implements Runnable {

	private ArrayList<Socket> sockets = new ArrayList<Socket>();
	ArrayList<String> files = new ArrayList<String>();
	private String path;
	private String format = ".java";

	public Pusher(ArrayList<Socket> sockets, String path) {
		this.path = path;
		this.sockets = sockets;
	}
	
	public Pusher(ArrayList<Socket> sockets, String path,String format) {
		this.path = path;
		this.sockets = sockets;
		this.format = format;
	}

	@Override
	public void run() {
		if (sockets.isEmpty()) {

		} else {
			try {
				files = getFiles();
				for (String file : files) {
					FileToString fts = new FileToString(path + "\\" + file);
					String filestring = fts.createString();

					for (Socket socket : sockets) {
						SendFile sf = new SendFile(socket,file,filestring);
						boolean response = sf.sendFile(path);
						System.out.println(response);

					}

				}
			} catch (Exception e) {

			}
		}
		try {
			Thread.sleep(1000*5);
		} catch (Exception e) {
			
		}
		run();
	}


	public ArrayList<String> getFiles() {
		FileList ls = new FileList(path,format);
		return ls.createFileList();
	}


}
