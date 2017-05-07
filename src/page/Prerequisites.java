package page;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import file.UniandesFile;
import java.time.Clock;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
import object.Course;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Prerequisites {

    private static Set<Course> coursesList = new HashSet<>();

    public static void crawling() {
        Document doc;
        try {
            String MAIN_URL = "https://registroapps.uniandes.edu.co/scripts/semestre/adm_con_prerrequisitos_joomla.php";
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
        new UniandesFile(UniandesFile.PREREQUISITES, coursesList);
        new UniandesFile(UniandesFile.COREQUISITES, coursesList);
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
                .select("body > table > tbody > tr > td > table > tbody > tr > td > table > tbody > tr:nth-child(4) > td > table > tbody > tr");
        if (!courses.isEmpty()) {
            for (Element courseColumn : courses.subList(1, courses.size())) {
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
            }
        }

    }

    private static Course setRequisites(Elements requisites, Course course) {
        
        Set<String> requisitesList = new HashSet<>();
        Set<String> corequisitesList = new HashSet<>();
        
        System.out.println("course: " + course.getName());       
        String requisitesString = requisites.text();
        //String requisitesString = "GEOC 1022 Y ( (QUIM 1101 Y QUIM 1102) O QUIM 1103) Y QUIM 1103* Y ( (QUIM 1101 Y QUIM 1102) O QUIM 1103) Y FISI 1028";
        //System.out.println("requisites: " + requisitesString);
        
        try{
            ArrayList outers = new ArrayList();
            
            ArrayList newOuters = getOuterYs(requisitesString, outers);
            ArrayList otherOuters = new ArrayList();

            String part = ""; String prereq = ""; String coreq = "";
            int i;
            for (i=0; i<newOuters.size(); i++){
                part = (String) newOuters.get(i);
                part = removeParenthesis(part);
                if (areThereYs(part)){
                    otherOuters = getOuterYs(part, otherOuters);
                }
                else {
                    prereq = getFirstO(part);
                    if (isCoreq(prereq)){
                        coreq = prereq.split("\\*")[0];
                        corequisitesList.add(coreq);
                        System.out.println("coreq: " + coreq);
                    }
                    else {
                        requisitesList.add(prereq);
                        System.out.println("req: " + prereq);
                    }
                }
            }

            //ArrayList inners = new ArrayList();
            String innerYs[];
            int j;
            for (i=0; i<otherOuters.size(); i++){
                part = (String) otherOuters.get(i);
                part = removeParenthesis(part);
                part = getFirstO(part);
                part = removeParenthesis(part);
                //inners = getOuterYs(part, inners);

                if (areThereYs(part)){
                    innerYs = part.split(" Y ");
                    for (j=0; j<innerYs.length; j++){
                        prereq = getFirstO(innerYs[j]);
                        if (isCoreq(prereq)){
                            coreq = prereq.split("\\*")[0];
                            corequisitesList.add(coreq);
                            System.out.println("coreq: " + coreq);
                        }
                        else {
                            requisitesList.add(prereq);
                            System.out.println("req: " + prereq);
                        }
                    }
                }
                else {
                    //prereq = getFirstO(part);
                    prereq = part;
                    if (isCoreq(prereq)){
                        coreq = prereq.split("\\*")[0];
                        corequisitesList.add(coreq);
                        System.out.println("coreq: " + coreq);
                    }
                    else {
                        requisitesList.add(prereq);
                        System.out.println("req: " + prereq);
                    }
                }
            }
        } catch (EmptyStackException e){
            String firstO = getFirstO(requisitesString);
            firstO = removeParenthesis(firstO);
            String prereq, coreq; 
            
            if (areThereYs(firstO)){
                String innerYs[] = firstO.split(" Y ");
                int j;
                
                for (j=0; j<innerYs.length; j++){
                    prereq = innerYs[j];
                    if (isCoreq(prereq)){
                        coreq = prereq.split("\\*")[0];
                        corequisitesList.add(coreq);
                        System.out.println("coreq: " + coreq);
                    }
                    else {
                        requisitesList.add(prereq);
                        System.out.println("req: " + prereq);
                    }
                }
            }
            else {
                prereq = firstO;
                if (isCoreq(prereq)){
                    coreq = prereq.split("\\*")[0];
                    corequisitesList.add(coreq);
                    System.out.println("coreq: " + coreq);
                }
                else {
                    requisitesList.add(prereq);
                    System.out.println("req: " + prereq);
                }
            }
        }      
        
        course.setRequisites(requisitesList);
        course.setCorequisites(corequisitesList);
        return course;
    }
    
    private static boolean areThereYs(String p) {
        if (p.split("Y").length > 1){
            return true;
        }
        return false;
    }
    
    private static boolean isCoreq(String p) {
        CharSequence sq = "*";
        if (p.contains(sq)){
            return true;
        }
        return false;
    }
    
    private static ArrayList getOuterYs(String p, ArrayList outers) {
        
        int numParenthesis = 0;
        int newPosY = 0;
        
        Stack ys = new Stack();
        
        int i; int cutter = 0;
        String l = "";
        String newString = p;
        String letters[] = p.split(""); 
        String part1; String part2;
        
        for (i=0; i<letters.length; i++){
            l = letters[i];
            if (l.equals("(")){
                ys.push(1);
                numParenthesis += 1;
            }
            else if (l.equals(")")){
                ys.pop();
            }
            else if (ys.isEmpty() && (l.equals("Y")) && (letters[i-1].equals(" ")) && (letters[i+1].equals(" "))){
                
                newPosY = i-cutter;
                                
                part1 = newString.substring(0, newPosY-1);
                part2 = newString.substring(newPosY+2);

                outers.add( part1 );
                newString = part2;
                
                cutter += part1.length() + 3;
            }           
        }
        
        outers.add(newString);
        
        return outers;
    }
    
    private static String removeParenthesis(String s){
        
        String letters[] = s.split("");
        
        if (letters[0].equals("(") && letters[letters.length-1].equals(")")){
            String newS = "";
            int i;
            for (i=1; i<letters.length-1; i++){
                newS += letters[i];
            }
            return newS.trim();
        }
        else{
            return s.trim();
        }
    }

    private static String getFirstO(String s) {
        return s.split(" O ")[0];
    }
}