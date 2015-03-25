package beta.server;

import java.io.BufferedReader;
import java.io.FileReader;

public class FileToString {

	private String path;
	
	public FileToString(String path) {
		this.path = path;
	}
	

	public String createString() {
		String string = "";

		try {
		FileReader file = new FileReader(path);
		BufferedReader reader = new BufferedReader(file);
	
		String line;
		
		while ((line = reader.readLine()) != null) {
			string += "\n" + line;
		}
		
		
		file.close();
		reader.close();
		} catch (Exception e) {
			return null;
		}

		return string;
	}
	

	public static void main(String[] args) {
		FileToString fts = new FileToString("C:\\Users\\Aleksander\\Dropbox\\Public\\NTNU\\Java\\workspace\\KTN Chat\\src\\server\\Server.java");
		String file = fts.createString();
		System.out.println(file);
	}
	
}
