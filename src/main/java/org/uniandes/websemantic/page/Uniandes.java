package org.uniandes.websemantic.page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.uniandes.websemantic.file.ArtistFile;
import org.uniandes.websemantic.object.Course;

public class Uniandes {

	private static String MAIN_URL = "https://registro.uniandes.edu.co/index.php/nuevos/prerrequisitos-cursos";
	private static ArrayList<Course> coursesList;

	public static void crawling(){
		Document doc;
		try {
			String url="";
			doc = Jsoup.connect(MAIN_URL).get();
			Elements links = doc.select("font.text4 a");
			for (Element link : links) {
				url = link.attr("abs:href");
				String titulo = link.text();
				subPage(url,titulo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		createFile();
	}


	private static void createFile() {
			new ArtistFile("Uniandes", coursesList);
	}


	private static void subPage(String url, String nombre) throws IOException {
		try {
			Document doc = Jsoup.connect(url).get();
			pageDepartment(doc);
		} catch (Exception e) {
			System.err.println(e.getMessage()+" url: "+url);
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
		Elements courses = doc.select("body > table > tbody > tr > td > table > tbody > tr > td > table > tbody > tr:nth-child(4) > td > table > tbody > tr");
		if(!courses.isEmpty()){
			for (Element courseColumn : courses) {
				Course course = new Course();
				Elements courseCell = courseColumn.select("td");
				course.setCode(courseCell.get(1).select("font span").text());
				course.setName(courseCell.get(2).select("font").text());
				//TODO Otros valores
				coursesList.add(course);
			}	
		}

	}
}