package page;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import file.UniandesFile;
import object.Course;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AllCourses {

    private static Set<Course> coursesList = new HashSet<>();

    public static void crawling() {
        Document doc;
        try {
            String MAIN_URL = "https://registroapps.uniandes.edu.co/scripts/semestre/adm_con_horario_joomla.php";
            String url;
            doc = Jsoup.connect(MAIN_URL).get();
            Elements links = doc.select("font.texto4 a");
            System.out.println(links.size());
            for (Element link : links) {
                url = link.attr("abs:href");
                subPage(url);
            }
            createFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createFile() {
        new UniandesFile(UniandesFile.ALL_COURSES, coursesList);
    }

    private static void subPage(String url) throws IOException {
        try {
            Document doc = Jsoup.connect(url).get();
            pageDepartment(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void pageDepartment(Document doc) {
        Elements courses = doc
                .select("body > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td > table > tbody > tr:nth-child(3n+1) > td > table > tbody > tr:nth-child(2)");
        if (!courses.isEmpty()) {
            for (Element courseColumn : courses) {
                Course course = new Course();
                Elements courseCell = courseColumn.select("td");
                course.setCode(courseCell.get(1).select("font").text());
                try {
                    course.setCredits(Double.parseDouble(courseCell.get(3).select("div").text()));
                } catch (Exception e) {
                    course.setCredits(0);
                }
                course.setName(courseCell.get(4).select("font").text());
                coursesList.add(course);
            }
        }

    }
}