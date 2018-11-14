package v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;


public class Extractor {

	public String SOURCE_PATH = new String();
	public String DESTINATION_PATH = new String();
	public final String FILE_TYPE = new String();
	public  Integer index = 0;
	public File file = new File(SOURCE_PATH);
	public  FilePathGui GUI;
	
	File[] directories = new File(SOURCE_PATH).listFiles(File::isDirectory);

	public Extractor() {

		
	}
	
	public void beginExtraction() {
		
		SOURCE_PATH = GUI.getSourcePath();
		directories = new File(SOURCE_PATH).listFiles(File::isDirectory);
		file = new File(SOURCE_PATH);
		DESTINATION_PATH = GUI.getDestPath();
		
		addLatestChildDirectory(directories);
		
			if(GUI.getIsSpecifiedType()) {
				extractDesignatedFileType(GUI.getFileTypeText());
			} else {
				extractAllFileTypes();
			}		
	}

	public void extractDesignatedFileType(String fileType){
		for(File f : directories) {
			File[] fileTypes =  f.listFiles(new FilenameFilter() { 
				public boolean accept(File dir, String filename)
				{ return filename.endsWith(fileType); } 
			} );

			for(File pdf : fileTypes){
				extractPdf(pdf.getAbsolutePath());
			}
		}
	}

	public void extractAllFileTypes(){
		for(File f : directories) {
			File[] fileTypes =  f.listFiles(new FilenameFilter() { 
				public boolean accept(File dir, String filename)
				{ return filename != null; }
			} );

			for(File pdf : fileTypes){
				extractPdf(pdf.getAbsolutePath());
			}
		}
	}

	public void addLatestChildDirectory(File[] directory){
		File[] newChildren = new File[0];
		for(File f : directory){
			newChildren = ArrayUtils.addAll(newChildren, f.listFiles(File::isDirectory));
		}
		System.out.println("Found Child Directories: "+ Arrays.toString(newChildren));

		directories = ArrayUtils.addAll(directories, newChildren); 
		int size = +newChildren.length;
		System.out.println("SIZE : "+size);
		if(size!=0){ 
			addLatestChildDirectory(newChildren);
		}
	}

	public String getPdfName(String filePath){
		File f = new File(filePath);
		return (f.getName());
	}

	public void extractPdf(String filePath){
		InputStream inStream = null;
		OutputStream outStream = null;

		try{
			File currentFile =new File(filePath); // pdf being cut
			File currentDirectory =new File(DESTINATION_PATH+(""+index+++" ")+getPdfName(filePath)); 
			
			// "cut" the files being extracted
			// Files.move(aPdf, pdfs, REPLACE_EXISTING);  
			
			inStream = new FileInputStream(currentFile);
			outStream = new FileOutputStream(currentDirectory);

			byte[] buffer = new byte[1024];
			int length;

			//copy the file content in bytes 
			while ((length = inStream.read(buffer)) > 0){
				outStream.write(buffer, 0, length);
			}

			inStream.close();
			outStream.close();

			//delete the original file
			//aPdf.delete();

			System.out.println("File is copied successful!");

		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public FilePathGui getGUI() {
		return GUI;
	}

	public void setGUI(FilePathGui gUI) {
		GUI = gUI;
	}
	
	

}
