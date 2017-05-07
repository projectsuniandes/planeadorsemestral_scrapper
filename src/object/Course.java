package object;

import java.util.HashSet;
import java.util.Set;

public class Course {

    private String code, name;

    private double credits;

    private Set<String> prerequisites;
    private Set<String> corequisites;

    public Course() {

        super();
        prerequisites = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<String> getRequisites() {
        return prerequisites;
    }

    public void setRequisites(Set<String> prerequisites) {
        this.prerequisites = prerequisites;
    }
    
    public Set<String> getCorequisites() {
        return corequisites;
    }
    
    public void setCorequisites(Set<String> corequisites) {
        this.corequisites = corequisites;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return "{ code: " + code + ",\n"
                + "name: " + name + ",\n"
                + "credits" + credits + ", \n"
                + "prerequisites: " + prerequisites;
    }
}
