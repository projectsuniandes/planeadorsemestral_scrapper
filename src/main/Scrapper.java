package main;

import page.AllCourses;
import page.Prerequisites;

public class Scrapper {

    public static void main(String[] args) {
        Prerequisites.crawling();
        AllCourses.crawling();
    }
}
