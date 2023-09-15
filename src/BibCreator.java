import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


/*
 * Java Final Project Winter 2023
 * Mohammad Safaeirad - 2133075
 * Lingfang He
 * */

public class BibCreator {

	public static void main(String[] args) throws FileInvalidException {
		// TODO Auto-generated method stub
		BibCreator bibFiles = new BibCreator();
		bibFiles.processFilesForValidation();
	}
	
	public static final String FILE_PATH = "./Final_DATA";	
	public static final int MAX_READ_TRY = 2;
	public static final String ARTICLE_END = "}";
	
	File[] myBibFiles; 
	ArrayList<Article> myArts;
	
	// check if bib file exist
	public void checkFileExist(String dirpath) {		
		String[] fileNamesList = new String[10];
		for (int i = 1; i < 11; i++) {
			fileNamesList[i-1] = "Latex" + i + ".bib";
		}	
		for (int i = 1; i < 11; i++) {
			
			File file = new File(dirpath, fileNamesList[i-1]); // create file with (path, name)
			if(file.exists()) {
				//continue;
			}else {
				System.out.println("Could not open input file " + file 
				+ " for reading. \nPlease check if file exists! "
				+ "+\nProgram will terminate after closing any opened files.");
				System.exit(1);
			}			
		}		
	}
	
	public File[] readBibDir(String dirPath) {
		File files = new File(dirPath);
		myBibFiles = files.listFiles();
		checkFileExist(dirPath);		
		return myBibFiles;
	}
	public boolean checkFileType(File file) {
		String[] fname = file.getName().split("\\.");
		String checkSample = fname[0].replace("Latex", "");
		String fType = fname[1]; 
		if(!fType.equals("bib") || !checkID(checkSample)) {		 
			return false;	 
		}else {
			return true; 
		}	 
	}
	//************************
	public void processFilesForValidation()  {
		 myBibFiles = readBibDir(FILE_PATH);
		 
		 for(File file: myBibFiles) {
			 if(checkFileType(file)) {			 
				System.out.println("\nProcessing bib file : " + file.getName() + " ......"); 			
				try {
					myArts = readBibFiles(file);
					JSONCreator(myArts,file);
				} catch (FileInvalidException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			 }
		 }
		 
		 displayAllJSON();
	 }
	//************************
	private void displayAllJSON() {
		// TODO Auto-generated method stub
		 Scanner scanner = new Scanner(System.in);
			 
			int remainingChance = MAX_READ_TRY;			
			String fileName;			
			FileReader fileReader;					
			while(remainingChance > 0)
			{
				System.out.print("\nPlease enter the name of one of the files that you need to review (Write EXIT to exit): ");				
				fileName = (scanner.next().trim()).toUpperCase();	// convert all input to uppercase
				String fn = fileName;
				if(fileName.equals("EXIT"))
				{
					System.out.println("*******************************************************"
							+ "\nGood bye, hope you have enjoyed creating the needed file with BibCreator ^v^");				
					System.exit(1);				
				}			
				fileName = FILE_PATH + "\\" + fileName;				
				System.out.println("\nHere are the contant of successfully created JSON file:   " + fn);				
				File jsonFile = new File(fileName);			
				if(jsonFile.exists())
				{		
					try
					{
						fileReader = new FileReader(fileName);
						BufferedReader br = new BufferedReader(fileReader);						
						String fileLine;									
						while((fileLine = br.readLine()) != null)
						{
							System.out.println(fileLine);	
						}					
						br.close();
					}catch(IOException e) {		 				
					}	
				}
				else
				{		
					remainingChance = remainingChance - 1;					
					System.out.println("Could not open input file. File does not exist; Remaining chance : " + remainingChance);
				}		
			} 		
			scanner.close();			
			System.out.println("Sorry! The files cannot be displayed! Program is ending ...!");		
			System.exit(1);			
	}
	private void JSONCreator(ArrayList<Article> myArticles, File file) {
		// TODO Auto-generated method stub
		String fileName = file.getName();		
		String fileNumberStr = fileName.replaceAll("Latex", "");
		fileNumberStr = fileNumberStr.replaceAll(".bib", "");
	
		int fileNumber = Integer.parseInt(fileNumberStr);			
		String IEEEFileName = "\\IEEE" + fileNumber + ".json";
		String ACMFileName = "\\ACM" + fileNumber + ".json";
		String NJFileName = "\\NJ" + fileNumber + ".json";			
		try
		{
			int counter = 1;
			PrintWriter printWriter = new PrintWriter(new FileWriter(FILE_PATH + IEEEFileName));
				
			for(Article art: myArticles)
			{
				String author = art.getAuthor();
				String title = art.getTitle();
				String journal = art.getJournal();
				String volume = art.getVolume();
				String number = art.getNumber();
				String pages = art.getPages();
				String month = art.getMonth();
				String year = art.getYear();
				
				printWriter.println(author + ". \"" + title + "\"," + journal + ", vol. " + volume + ", no. " + number
						+ ", p. " + pages +", " + month + " " + year + ".");				
				printWriter.println("");
			}			
			FileWriter writer2 = new FileWriter(FILE_PATH + ACMFileName);
			PrintWriter printWriter2 = new PrintWriter(writer2);
				
			for(Article art: myArticles)
			{
				String author = art.getAuthor();
				String firstAuthor = author.split(",")[0];
				String title = art.getTitle();
				String journal = art.getJournal();
				String volume = art.getVolume();
				String number = art.getNumber();
				String pages = art.getPages();
				String year = art.getYear();
				String doi = art.getDoi();
				
				printWriter2.println("[" + counter +"] " + firstAuthor + " et al. " + year + ". " + title + ". " + journal + ". " + volume + ", " 
						+ number + "(" + year + "), " + pages + ". DOI:http:" + doi + ".");				
				printWriter2.println("");					
				counter = counter + 1;
			}				
			FileWriter writer3 = new FileWriter(FILE_PATH + NJFileName);
			PrintWriter printWriter3 = new PrintWriter(writer3);
				
			for(Article art: myArticles)
			{
				String author = art.getAuthor().replaceAll(",", " & ");
				String title = art.getTitle();
				String journal = art.getJournal();
				String volume = art.getVolume();
				String pages = art.getPages();
				String year = art.getYear();
				
				printWriter3.println(author + ". " + title + ". " + journal + ". " + volume + ", " + pages + "(" + year + ").");				
				printWriter3.println("");
			}				
			printWriter.close();
			printWriter2.close();
			printWriter3.close();
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}	
	}

	private ArrayList<Article> readBibFiles(File file) throws FileInvalidException {
		// TODO Auto-generated method stub
		ArrayList<Article> myArts = new ArrayList<Article>();
	 	ArrayList<String> articleInfos = new ArrayList<String>();
	 	
	 	FileReader fileReader;		 	
	 	try {
	 		
			fileReader = new FileReader(file);
			BufferedReader read = new BufferedReader(fileReader);
			String bibLine;
			while((bibLine = read.readLine()) != null) {
				if(bibLine.equals("")) {
					continue;
				}
				ArticleVerifier(file, bibLine);
				if(bibLine.startsWith(ARTICLE_END)) {
					myArts.add(createArticles(articleInfos));
					articleInfos = new ArrayList<String>();
				}else {
					
					articleInfos.add(bibLine);
				}
			}				
			read.close();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myArts;	
	}

	private Article createArticles(ArrayList<String> articleStr) {		
		Article result = new Article();		
		for(String str: articleStr)
		{		
			//Remove any extra spaces
			str = str.trim();		
			String subStr = "";			
			if(str.startsWith("@ARTICLE{") || str.startsWith("}") || str.equals("") || str.startsWith("﻿@ARTICLE{"))
			{
				continue;			
			}				
			if(!str.equals("}"))
			{
				subStr = str.substring(0, str.length()-1); 
				if(checkID(subStr))
				{					
					String ID = subStr;				
					result.setId(ID);					
					continue;
				}
			}			
			String[] elementTokens = subStr.split("=");				
			String elementName = elementTokens[0];
			String value = elementTokens[1].substring(1, elementTokens[1].length()-1).trim();									
			switch(elementName)
			{
				case "author":
					result.setAuthor(value);
					break;
				case "journal":  
					result.setJournal(value);
					break;
				case "title":
					result.setTitle(value);
					break;
				case "year":
					result.setYear(value);
					break;
				case "volume":
					result.setVolume(value);
					break;
				case "number":
					result.setNumber(value);
					break;
				case "pages":
					result.setPages(value);
					break;
				case "keywords":
					result.setKeywords(value);
					break;
				case "doi":
					result.setDoi(value);
					break;
				case "ISSN":
					result.setIssn(value);
					break;
				case "month":
					result.setMonth(value);
					break;
				default:
					System.out.println("Missing article info: " + elementName);
			}	
		}			
		return result;	
	}

	private boolean ArticleVerifier(File file, String bibLine) throws FileInvalidException {
		 bibLine = bibLine.trim(); // remove the useless space 
		 String info = "";
		 if(bibLine.startsWith("@ARTICLE{") || bibLine.startsWith("}")||
				 bibLine.equals("")) {
			 return true;
		 }
		 if(!bibLine.startsWith("}")) {
			 info = bibLine.substring(0, bibLine.length()-1); // get the id for an Article
			 if(checkID(info)) {
				 return true;
			 }
		 }		 
		 String[] line = info.split("=");
		 String value = line[1].substring(1, line[1].length()-1);
		 if(value.equals("")) {
			 System.out.println("Field: " + line[0] + " has empty value");
			 String error = line[0];			 
			 throw new FileInvalidException(file, error);
		 }else {
			 
		 } 
		 return true;
	}
	private boolean checkID(String id) {
		try {
			Integer.parseInt(id);
			return true;
		}catch(Exception e) {
			//System.out.println(e.getMessage());
		}
		return false;
	}	

}

