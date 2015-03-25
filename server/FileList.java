package beta.server;

import java.io.File;
import java.util.ArrayList;

public class FileList {

	private String path;
	private ArrayList<String> fileList = new ArrayList<String>();
	private String format = ".java";
	
	public FileList(String path) {
		this.path = path;
	}
	
	public FileList(String path,String format) {
		this.path = path;
		this.format = format;
	}
	
	public ArrayList<String> createFileList() {
		
		ArrayList<String> files = new ArrayList<String>();
		try {
		File file = new File(path);
		
		String[] dirContent =  file.list();
		
		for (String s : dirContent) {
			if (s.contains(format)) {
				files.add(s);
			}
		}
		
		} catch (Exception e) {
			return null;
		}
		return files;
	}
	
	
	public static void main(String[] args) {
		FileList ls = new FileList("C:\\Users\\Aleksander\\Dropbox\\Public\\NTNU\\Java\\workspace\\KTN Chat\\src\\server\\");
		ArrayList<String> list = ls.createFileList();
		for (String s : list) {
			System.out.println(s);
		}
	}
	
}
