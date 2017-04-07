package org.uniandes.websemantic.file;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.uniandes.websemantic.object.Artist;
import org.uniandes.websemantic.object.Artwork;
import org.uniandes.websemantic.object.Course;

public class UniandesFile {

	//Delimiter used in CSV file
	private static final String NEW_LINE_SEPARATOR = "\n";

	CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
	
	private static final Object [] FILE_HEADER = 
	{"name","code","requisites"};

	CSVPrinter csvFilePrinter = null;

	FileWriter fileWriter = null;		

	public UniandesFile(String pageName){		
		init(pageName);
	}

	public UniandesFile(String pageName, Set<Course> courses) {
		try {
			init(pageName);
			for (Course course : courses) {
				writeCourse(course);
			}
			closeFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inicializa las variables
	 * @param pageName
	 */
	private void init(String pageName) {
		//Create the CSVFormat object with "\n" as a record delimiter
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

		try {
			//initialize FileWriter object
			fileWriter = new FileWriter("artist"+pageName+".csv");

			//initialize CSVPrinter object 
			csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);

			//Create CSV file header
			csvFilePrinter.printRecord(FILE_HEADER);

			System.out.println("CSV file was created successfully "+pageName+"!!!");

		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
			closeFile();
		}
	}


	/**
	 * Escribe en el archivo de artistas la informacion del artista
	 * @param artist datos del artista
	 * @throws IOException en caso que existan problemas con el lector del artista
	 */
	public void writeCourse(Course course) throws IOException{
		List<String> courseRecord = new ArrayList<String>();
		courseRecord.add(course.getCode());
		courseRecord.add(course.getName());
		courseRecord.add(course.getRequisites().toString());
		csvFilePrinter.printRecord(courseRecord);
	}

	public void closeFile(){
		try {
			fileWriter.flush();
			fileWriter.close();
			csvFilePrinter.close();
		} catch (IOException e) {
			System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
			e.printStackTrace();
		}

	}
}
