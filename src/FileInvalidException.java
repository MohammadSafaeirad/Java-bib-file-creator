import java.io.File;

public class FileInvalidException extends Exception {

	private static final long serialVersionUID = 1L;
	private String error;
	String fileName;

	public FileInvalidException(String error) {
		this.error = error;
		this.fileName = "";
	}
	public FileInvalidException(File file,String error) {
		this.error = error;
		this.fileName = file.getName() ; 
		displayError(); 
	}
	private void displayError() {
		
		System.out.println("\nError: Detected Empty Field! \n=============================");
		System.out.println("Problem detected with input file: " + fileName );
		System.out.println("\nFile is Invalid: Field " + error 
				+ " is Empty. Processing stoped at this point. Other empty fields maybe present as well!!");
	}

}

