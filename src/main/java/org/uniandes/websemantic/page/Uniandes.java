package org.uniandes.websemantic.page;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.uniandes.websemantic.file.UniandesFile;
import org.uniandes.websemantic.object.Course;

public class Uniandes {

	private static String MAIN_URL = "https://registroapps.uniandes.edu.co/scripts/semestre/adm_con_prerrequisitos_joomla.php";
	private static Set<Course> coursesList = new HashSet<>();

	public static void crawling() {
		Document doc;
		try {
			String url = "";
			doc = Jsoup.connect(MAIN_URL).get();
			Elements links = doc.select("font.texto4 a");
			System.out.println(links.size());
			for (Element link : links) {
				url = link.attr("abs:href");
				String titulo = link.text();
				subPage(url, titulo);
			}
			createFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void createFile() {
		new UniandesFile("Uniandes", coursesList);
	}

	private static void subPage(String url, String nombre) throws IOException {
		try {
			Document doc = Jsoup.connect(url).get();
			pageDepartment(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * 
	 * @param doc
	 * @param artist
	 * @return
	 */
	private static void pageDepartment(Document doc) {
		Elements courses = doc
				.select("body > table > tbody > tr > td > table > tbody > tr > td > table > tbody > tr:nth-child(4) > td > table > tbody > tr");
		if (!courses.isEmpty()) {
			for (Element courseColumn : courses.subList( 1, courses.size() )) {
				Course course = new Course();
				Elements courseCell = courseColumn.select("td");
				course.setCode(courseCell.get(0).select("font span").text());
				course.setName(courseCell.get(1).select("font").text());
				Elements requisites = courseCell.get(2).select("font");
				Elements requisites2 = requisites.select("span");
				if (requisites2.isEmpty()) {
					course = setRequisites(requisites, course);
				} else {
					course = setRequisites(requisites2, course);
				}
				coursesList.add(course);
				System.out.println(course.toString());
			}
		}

	}

	private static Course setRequisites(Elements requisites, Course course) {
		Pattern pattern = java.util.regex.Pattern.compile("(\\w+[ ]*\\d+)");
		Matcher matcher = pattern.matcher(requisites.text());
		Set<String> requisitesList = new HashSet<String>();
		while (matcher.find()) {
			String courseCode = matcher.group(1);
			requisitesList.add(courseCode);
		}
		course.setRequisites(requisitesList);
		return course;
	}
}