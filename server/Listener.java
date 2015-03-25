package beta.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Listener implements Runnable {


	private static final int TCP_PORT = 5000;
	
	public static boolean interrupted = false;

	private ArrayList<Socket> sockets = new ArrayList<Socket>();
	//private ArrayList<Thread> threads = new ArrayList<Thread>();
	private ServerSocket welcomeSocket;
	private String path;
	private String format = ".java";

	public Listener(String path) {
		this.path = path;
	}
	
	public Listener(String path,String format) {
		this(path);
		this.format = format;
	}
	public Listener(String path,String format,ArrayList<Socket> sockets) {
		this(path);
		this.format = format;
		this.sockets = sockets;
	}

	
	@Override
	public void run() {
		push();
		start();
		// listen het før run
	}

	public void exit() {
		System.out.println("Exiting");
	}
	
	public void start() {
		try {
			
			this.welcomeSocket = new ServerSocket(TCP_PORT);
			System.out.println("Server started. Waiting for connection");
			listen();
			
		} catch (Exception e) {

		}
		
	}


	public void listen() {

		
		while (true) {
			try {
			
				Socket connectionSocket = welcomeSocket.accept();
				sockets.add(connectionSocket);
				System.out.println("Incoming connection from  "+ connectionSocket.toString());
			} catch (Exception e) {

			
			exit();
		}
	}
	}
	
	public void push() {
		Runnable pusher = new Pusher(sockets, path,format);
		Thread thread = new Thread(pusher);
		thread.start();
	}
	
	public int getSockets() {
		return sockets.size();
	}
	
	public static void main(String[] args) {

		String path  = "C:\\Users\\Aleksander\\Dropbox\\Public\\NTNU\\Java\\workspace\\KTN Chat\\src\\server\\";
		Listener listener = new Listener(path);
		//listener.start();
		listener.run();

	}
	
	

}
