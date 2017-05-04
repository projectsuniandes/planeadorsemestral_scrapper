package file;

import object.Course;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UniandesFile {

    //Delimiter used in CSV file
    private static final String NEW_LINE_SEPARATOR = "\n";
    public static final String PREREQUISITES = "Prerequisites";
    public static final String ALL_COURSES = "Uniandes";


    private CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

    private CSVPrinter csvFilePrinter = null;

    private FileWriter fileWriter = null;

    public UniandesFile(String pageName, Set<Course> courses) {
        try {
            init(pageName);
            for (Course course : courses) {
                if (pageName.equals(PREREQUISITES))
                    writeCoursePre(course);
                else if (pageName.equals(ALL_COURSES))
                    writeCourse(course);
            }
            closeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init(String pageName) {

        try {
            //initialize FileWriter object
            fileWriter = new FileWriter("courses" + pageName + ".csv");

            //initialize CSVPrinter object
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);

            //Create CSV file header
            if (pageName.equals(PREREQUISITES)) {
                Object[] FILE_HEADER =
                        {"name", "code", "requisites"};
                csvFilePrinter.printRecord(FILE_HEADER);
            } else {
                Object[] FILE_HEADER =
                        {"name", "code", "credits"};

                csvFilePrinter.printRecord(FILE_HEADER);
            }

            System.out.println("CSV file was created successfully " + pageName + "!!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
            closeFile();
        }
    }

    private void writeCoursePre(Course course) throws IOException {
        List<String> courseRecord = new ArrayList<>();
        courseRecord.add(course.getCode());
        courseRecord.add(course.getName());
        courseRecord.add(course.getRequisites().toString());
        csvFilePrinter.printRecord(courseRecord);
    }

    private void writeCourse(Course course) throws IOException {
        List<String> courseRecord = new ArrayList<>();
        courseRecord.add(course.getCode());
        courseRecord.add(course.getName());
        courseRecord.add(String.valueOf(course.getCredits()));
        csvFilePrinter.printRecord(courseRecord);
    }

    private void closeFile() {
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
